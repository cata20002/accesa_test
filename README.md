# accesa_test
# Price Comparator - Backend Implementation

## Project Overview

This project is a backend implementation of a "Price Comparator - Market" application that helps users compare prices of grocery items across different supermarket chains (Lidl, Kaufland, Profi). The system enables users to track price changes, find the best deals, and optimize their shopping experience.

## Features to be Implemented

### 1. Daily Shopping Basket Optimization

The system can take a user's shopping list and split it into store-specific lists to optimize for cost savings. It analyzes current prices and ongoing discounts to recommend the most cost-effective distribution of purchases across stores.

### 2. Best Discounts Finder

The application identifies and lists products with the highest percentage discounts across all tracked stores, helping users spot the best current deals. The output is a map with the 5 biggest discounts per store.

### 3. New Discounts Alert

The system tracks and presents discounts that have been newly added within the last 24 hours, allowing users to stay updated on new promotional offers. This can be a push notification in the frontend later down the line.

### 4. Dynamic Price History

The backend provides data points that enable visualization of price trends over time for individual products. This data can be filtered by store, product category, or brand.

### 5. Value Analysis & Product Substitutes

The system calculates and highlights "value per unit" (e.g., price per kg, price per liter) to help identify the best buys even when pack sizes differ. For this, a product is selected and the output is a list with all its available versions from all stores and the price/unit.

### 6. Custom Price Alerts

Users can set target prices for specific products. The system will identify when a product's price drops to or below that target.

## Technical Architecture

### Project Structure

```
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── test
    │   │           └── accesa
    │   │               ├── controller/
    │   │               ├── dto/
    │   │               ├── entity/
    │   │               ├── repository/
    │   │               ├── service/
    │   │               └── AccesaApplication.java
    │   └── resources
    │       ├── application.properties
    │       └── static/
    │           ├── kaufland_2025-05-01.csv
    │           ├── kaufland_2025-05-08.csv
    │           ├── kaufland_discounts_2025-05-01.csv
    │           ├── kaufland_discounts_2025-05-08.csv
    │           ├── lidl_2025-05-01.csv
    │           ├── lidl_2025-05-08.csv
    │           ├── lidl_discounts_2025-05-01.csv
    │           ├── lidl_discounts_2025-05-08.csv
    │           ├── profi_2025-05-01.csv
    │           ├── profi_2025-05-08.csv
    │           ├── profi_discounts_2025-05-01.csv
    │           └── profi_discounts_2025-05-08.csv
    └── test
        └── java
            └── com
                └── test
                    └── accesa
                      
```

### Domain Model

#### Core Entities

- **Product**: Represents a product with attributes such as id, name, category, brand, package details, and price.
- **Discount**: Represents a discount on a product with attributes like percentage, start date, and end date.
Further entities, such as user and store can be added to better store the information, i.e. avoid data duplication.
Different DTO's are used for both the Product and Discount, based on the necessary information for each endpoint.

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.4.5
- **Build Tool**: Maven
- **Database**: MySQL
- **APIs**: RESTful API endpoints using Spring Web

## Setup and Installation

### Prerequisites

- JDK 17 or higher
- Maven 3.8 or higher
- Git

### Clone the Repository

```bash
git clone https://github.com/cata20002/accesa_test.git
```

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR file directly:

```bash
java -jar target/accesa_test-1.0.0.jar
```

By default, the application will start on port 8080.

## API Endpoints

### Shopping Basket Optimization

- **GET** `/api/v1/discounts/optimize`
  - Parameters:
    - `items`: List of shoppingItemDTO
  - Response: List of shopping items with the best prices across all stores

### Best Discounts

- **GET** `/api/v1/discounts/top5`
  - Parameters:
  - Response: List of products with highest 5 percentage discounts per store

### New Discounts

- **GET** `/api/discounts/new`
  - Parameters:
  - Response: List of newly added discounts (last 24 hours)

### Value Analysis

- **GET** `/api/v1/products/substitutes/{productID}`
  - Parameters:
    - `productID`
  - Response: Products similar to the one specified by the product id, sorted by value per unit

## Implementation Details

### Data Import Process

The application uses a CommandLineRunner to initially load data from CSV files:

```java
@Component
public class ImportRunner implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public void run(String... args) {
        // Import products and discounts from CSV files
        List<Product> profiProducts = parseCsvToProducts("src/main/resources/static/profi_2025-05-02.csv");
        // Save products to database
        
        List<Discount> profiDiscounts = parseDiscounts("src/main/resources/static/profi_discounts_2025-05-02.csv", productRepository);
        // Save discounts to database
        
        // Similar import for other stores and dates
    }
}
```

The import process can be disabled in production or when not needed by using Spring's conditional configuration:

```java
@Component
@ConditionalOnProperty(name = "import.data.enabled", havingValue = "true", matchIfMissing = false)
public class ImportRunner implements CommandLineRunner {
    // Implementation
}
```
Afterwards, testing is done through the endpoints specified above. A suite of tests as well as a walkthrough of the code are described in the video demo, available here: [**link**](https://youtu.be/CoVyP9KE-bQ)

## Future Enhancements

1. **User Authentication**: Implement user accounts and authentication to personalize features
2. **Shopping History**: Track user's past purchases and provide insights

