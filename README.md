# Stock Portfolio Service (Cloud Computing Assignment #1) This is a Spring Boot REST API for managing a stock portfolio, supporting CRUD operations and 
real-time stock price retrieval using the [API Ninjas Stock Price API](https://api-ninjas.com/api/stockprice). The service runs inside a Docker 
container and listens on port `5001` by default. --- ## Features - Manage stocks in a portfolio: add, update, delete, and retrieve stocks - Retrieve 
current stock price and compute stock value (shares × current price) - Compute total portfolio value based on latest prices - Uses external API for 
real-time stock prices - All data is stored in-memory (non-persistent, resets on restart) - Runs inside a Docker container on port `5001` --- ## API 
Endpoints | Endpoint | Method | Description | |------------------------|--------|----------------------------------------------| | `/stocks` | GET | 
List all stocks in the portfolio | | `/stocks` | POST | Add a new stock to the portfolio | | `/stocks/{id}` | GET | Retrieve a specific stock by ID | | 
`/stocks/{id}` | PUT | Update a specific stock by ID | | `/stocks/{id}` | DELETE | Delete a stock by ID | | `/stock-value/{id}` | GET | Get current 
value of a stock (`shares × price`) | | `/portfolio-value` | GET | Get total value of the portfolio | --- ## JSON Representation of a Stock Object 
```json { "id": "string", "name": "string", "symbol": "string", "purchase price": float, "purchase date": "DD-MM-YYYY", "shares": int } ``` Notes on 
JSON fields: - `id`: Unique string ID assigned by the server (not provided in POST requests) - `name`: Company name; if missing on POST, server returns 
`"NA"` - `symbol`: Stock ticker symbol (uppercase) - `purchase price`: Price paid per share, rounded to 2 decimal places - `purchase date`: Date of 
purchase in `DD-MM-YYYY` format; if missing on POST, server returns `"NA"` - `shares`: Number of shares owned --- ## Example JSON for Adding a Stock 
(POST `/stocks`) ```json { "symbol": "GOOG", "purchase price": 140.12, "shares": 14, "name": "Alphabet Inc.", "purchase date": "24-10-2023" } ``` --- 
## Example JSON Returned by GET `/stocks` ```json [ { "id": "1", "name": "Alphabet Inc.", "symbol": "GOOG", "purchase price": 140.12, "purchase date": 
"24-10-2023", "shares": 14 }, { "id": "2", "name": "Apple Inc.", "symbol": "AAPL", "purchase price": 183.63, "purchase date": "22-02-2024", "shares": 
19 } ] ``` --- ## Example JSON Returned by GET `/stock-value/{id}` ```json { "symbol": "AAPL", "ticker": 226.96, "stock value": 4312.24 } ``` --- ## 
Example JSON Returned by GET `/portfolio-value` ```json { "date": "15-10-2024", "portfolio value": 10500.47 } ``` --- ## Error JSON Responses | Status 
Code | JSON Example | Description | |-------------|----------------------------------------------------|---------------------------------| | 400 | 
`{"error": "Malformed data"}` | Bad request data | | 404 | `{"error": "Not found"}` | Resource not found | | 415 | `{"error": "Expected 
application/json media type"}` | Unsupported media type | | 500 | `{"server error": "Exception message or API error"}` | Server or API errors | --- ## 
Running the Service with Docker ### Prerequisites - Docker installed on your machine ### Build Docker Image ```bash docker build -t 
stock-portfolio-service . ``` ### Run Docker Container ```bash docker run -p 5001:5001 --env NINJA_API_KEY=your_api_key_here stock-portfolio-service 
``` - The container listens on port `5001` - Replace `your_api_key_here` with your actual [API Ninjas](https://api-ninjas.com) stock price API key --- 
## Interacting with the API You can use **curl** or **Postman** to interact with the API. ### Example: Add a stock (POST) ```bash curl -X POST 
http://localhost:5001/stocks \ -H "Content-Type: application/json" \ -d '{ "symbol": "GOOG", "purchase price": 140.12, "shares": 14, "name": "Alphabet 
Inc.", "purchase date": "24-10-2023" }' ``` ### Example: Get portfolio value (GET) ```bash curl http://localhost:5001/portfolio-value ``` --- ## 
Assignment Notes - Data is **not persistent**; restarting the container clears the portfolio - IDs are unique strings and never reused - The service 
fetches live stock prices from the API Ninjas Stock Price API - Make sure to include your API key in the container environment - The service listens on 
port `5001` (configurable) - Tested to meet all assignment requirements, including proper status codes and JSON responses --- ## Resources - [API 
Ninjas Stock Price API](https://api-ninjas.com/api/stockprice) - [API Ninjas Pricing & Signup](https://api-ninjas.com/pricing) --- ## Submission - 
Submit all source code and Dockerfile - Zip your project preserving directory structure - Test your Docker build and run commands before submission - 
Ensure the TA can run your container and issue REST calls without extra setup --- # License Your license info here (if any)

