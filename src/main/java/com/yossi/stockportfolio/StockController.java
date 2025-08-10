package com.yossi.stockportfolio;
import javax.validation.Valid;  // For @Valid
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StockController {
    private List<Stock> stocks = new ArrayList<>();
    private long counter = 1;

   // NEW: GET stock by ID **(ADDED)**
   @GetMapping("/stocks/{id}")
   public ResponseEntity<Stock> getStockById(@PathVariable("id") String id) {
       Stock stock = stocks.stream()
               .filter(s -> s.getId().equals(id))
               .findFirst()
               .orElse(null);

       if (stock == null) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
       }
       return new ResponseEntity<>(stock, HttpStatus.OK); // 200 OK
   }

   @PostMapping("/stocks")
public ResponseEntity<Stock> createStock(@RequestBody @Valid Stock stock) {
    try {
        // Log the request body
        System.out.println("POST request received with payload: " + stock);

        // Validate required fields
        if (stock.getSymbol() == null || stock.getPrice() == null || stock.getNumberOfShares() == null) {
            System.out.println("Missing required fields: 'symbol', 'purchasePrice', or 'shares'");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // Optional fields: 'name' and 'purchaseDate'
        if (stock.getDate() == null) {
            stock.setDate(LocalDate.now()); // Set today's date if not provided
        }

        // Assign a unique ID
        String idString = counter++ + "";
        stock.setId(idString);

        // Add the stock to the list
        stocks.add(stock);

        // Return the created stock object with status 201
        return new ResponseEntity<>(stock, HttpStatus.CREATED); // 201 Created
    } catch (Exception e) {
        // Log the exception and return 500 Internal Server Error
        System.err.println("Error occurred while processing the POST request: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
    }
}



    // GET all stocks -- *OK*
    @GetMapping("/stocks")
    public ResponseEntity<List<Stock>> getAllStocks() {
        try {
            // Log the request to fetch all stocks
            System.out.println("Fetching all stocks");

            // Return the list of stocks
            if (stocks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if no stocks
            }

            return new ResponseEntity<>(stocks, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error occurred while fetching stocks: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

   // PUT (update) a stock -- *OK*
   @PutMapping("/stocks/{id}")
public ResponseEntity<Stock> updateStock(@PathVariable("id") String id, @RequestBody @Valid Stock stock) {
    // Log the request
    System.out.println("PUT request received for stock ID: " + id);
    System.out.println("Updated stock data: " + stock);

    // Find the stock by ID
    Stock existingStock = stocks.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);

    if (existingStock == null) {
        // Return 404 if the stock is not found
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
    }

    // Update the stock's fields
    existingStock.setCompanyName(stock.getCompanyName());
    existingStock.setSymbol(stock.getSymbol());
    existingStock.setPrice(stock.getPrice());
    existingStock.setNumberOfShares(stock.getNumberOfShares());
    existingStock.setDate(stock.getDate()); // Only update if provided

    // Return the updated stock object
    return new ResponseEntity<>(existingStock, HttpStatus.OK); // 200 OK
}

// DELETE a stock by ID -- *OK*
@DeleteMapping("/stocks/{id}") 
public ResponseEntity<Void> deleteStock(@PathVariable("id") String id) {
     // Find the stock by ID
     Stock existingStock = stocks.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);

     if (existingStock == null) {
         // Return 404 if the stock is not found
         return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
     }

     // Remove the stock from the list
     stocks.remove(existingStock);

     // Return 204 No Content with no body as required
     return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
}

// NEW: GET stock value by ID -- *OK*
    @GetMapping("/stock-value/{id}")
    public ResponseEntity<Map<String, Object>> getStockValue(@PathVariable("id") String id) {
        try {
            Stock stock = stocks.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
            if (stock == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
            }

            Map<String, Object> stockPriceData = NinjaApiService.getStockPrice(stock.getSymbol());
            double stockValue = (double) stockPriceData.get("price") * stock.getNumberOfShares();
            
            Map<String, Object> response = new HashMap<>();
            response.put("symbol", stock.getSymbol());
            response.put("ticker", stockPriceData.get("price"));
            response.put("stock value", stockValue);
            
            return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Server error"), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    // NEW: GET total portfolio value  -- *OK*
    @GetMapping("/portfolio-value")
    public ResponseEntity<Map<String, Object>> getPortfolioValue() {
        try {
            double totalValue = 0;
            
            for (Stock stock : stocks) {
                Map<String, Object> stockPriceData = NinjaApiService.getStockPrice(stock.getSymbol());
                totalValue += (double) stockPriceData.get("price") * stock.getNumberOfShares();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("date", LocalDate.now().toString());
            response.put("portfolio value", totalValue);

            return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Server error"), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }


}
