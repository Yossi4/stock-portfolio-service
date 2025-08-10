# Stock Portfolio Service (Cloud Computing)

This is a Spring Boot REST API for managing a stock portfolio, supporting CRUD operations and real-time stock price retrieval using the API Ninjas 
Stock Price API. The service runs inside a Docker container and listens on port 5001 by default.

---

## Features

- Manage stocks in a portfolio: add, update, delete, and retrieve stocks  
- Retrieve current stock price and compute stock value (shares × current price)  
- Compute total portfolio value based on latest prices  
- Uses external API for real-time stock prices  
- All data is stored in-memory (non-persistent, resets on restart)  
- Runs inside a Docker container on port 5001  

---

## API Endpoints

| Endpoint            | Method | Description                           |
|---------------------|--------|-------------------------------------|
| `/stocks`           | GET    | List all stocks in the portfolio     |
| `/stocks`           | POST   | Add a new stock to the portfolio     |
| `/stocks/{id}`      | GET    | Retrieve a specific stock by ID      |
| `/stocks/{id}`      | PUT    | Update a specific stock by ID        |
| `/stocks/{id}`      | DELETE | Delete a stock by ID                  |
| `/stock-value/{id}` | GET    | Get current value of a stock          |
| `/portfolio-value`  | GET    | Get total value of the portfolio      |

---

## JSON Representation of a Stock Object

```json
{
  "id": "string",
  "name": "string",
  "symbol": "string",
  "purchase price": float,
  "purchase date": "DD-MM-YYYY",
  "shares": int
}

