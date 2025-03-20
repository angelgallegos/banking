# Banking assessment

Assessment by Angel Gallegos Andrade. 

## Configuration
The project is configured with h2 database by default and with mysql if running inside a docker container so no need to perform any configuration.

## Running the project
The easiest way
```shell
  ./gradlew build && java -jar build/libs/banking-0.0.1-SNAPSHOT.jar
```

Alternatively there is a docker compose file so if you are using docker just run:
```shell
    docker compose up --build
```

## baseUrl
Unless the configuration is changed, when running locally the base path should be:
http://localhost:8080/api/

## Create a new customer

To create a new Customer there is the next endpoint:

POST `{baseUrl}/customer`

That accepts the next payload:

```json
{
	"name": "Angel",
	"surname": "Gallegos"
}
```
The successful response(200 status) contains the next body:
```json
{
	"id": "4e8a641c-8fc8-4c07-b6c4-cca83223271c",
	"version": 0,
	"name": "Angel",
	"surname": "Gallegos"
}
```

## Create a new current Account

To create a new current Account use the next endpoint:

POST `{baseUrl}/account`

That accepts the next payload:

```json
{
  "initialCredit": 10.5,
  "customerID": "4e8a641c-8fc8-4c07-b6c4-cca83223271c"
}
```
The `customerID` is the id of the Customer returned on the create customer endpoint payload.

If the value of the initialCredit is bigger than 0 a new Transaction of type `DEPOSIT` is created.

The successful response(200 status) contains the next body:

```json
{
  "id": "06fe068e-1037-4079-825c-f1e05c269877",
  "version": 0,
  "balance": 10.5,
  "type": "CURRENT",
  "customer": {
    "id": "4e8a641c-8fc8-4c07-b6c4-cca83223271c",
    "version": 0
  },
  "transactions": [
    {
      "id": "f0d394a5-3eb5-475e-956e-e46161f4d76f",
      "version": 0,
      "amount": 10.5,
      "type": "DEPOSIT"
    }
  ]
}
```

## Create other Transactions

New transactions performed on an Account can be performed by using the next endpoint:

POST `{baseUrl}/transaction`

Accepted payload:

```json
{
	"amount": 10,
	"account": {
		"id": "06fe068e-1037-4079-825c-f1e05c269877"
	},
	"type": "TRANSFER"
}
```

Where the `account.id` is the value of the id in the response of the endpoint used to create a new Account.
The accepted Types of the transactions are:
  - `TRANSFER` .- Subtracts the amount out of the account
  - `DEPOSIT` .- Adds the amount to the account

## List all the Transactions of an Account

To retrieve a paginated list of Transactions that belong to an Account you can call the next endpoint:

GET `{baseUrl}/transaction/list/{accountId}`

Where the `accountId` path variable is the identifier of the Account.

The payload response looks like the following:

```json
{
	"_embedded": {
		"transactionDTOList": [
			{
				"id": "a7493c7e-614b-4b0a-affa-6512519eb5de",
				"version": 0,
				"amount": 10.00,
				"type": "TRANSFER",
				"_links": {
					"self": {
						"href": "http://localhost:8080/apitransaction/a7493c7e-614b-4b0a-affa-6512519eb5de"
					}
				}
			},
			{
				"id": "ebc8b1de-1f9d-4c6a-adc8-0876849e3f3d",
				"version": 0,
				"amount": 10.00,
				"type": "DEPOSIT",
				"_links": {
					"self": {
						"href": "http://localhost:8080/apitransaction/ebc8b1de-1f9d-4c6a-adc8-0876849e3f3d"
					}
				}
			},
			{
				"id": "f0d394a5-3eb5-475e-956e-e46161f4d76f",
				"version": 0,
				"amount": 10.50,
				"type": "DEPOSIT",
				"_links": {
					"self": {
						"href": "http://localhost:8080/apitransaction/f0d394a5-3eb5-475e-956e-e46161f4d76f"
					}
				}
			}
		]
	},
	"_links": {
		"self": {
			"href": "http://localhost:8080/api/transaction/list/06fe068e-1037-4079-825c-f1e05c269877?page=0&size=20"
		}
	},
	"page": {
		"size": 20,
		"totalElements": 3,
		"totalPages": 1,
		"number": 0
	}
}
```

## List all Accounts of a Customer

To get a paginated list of the Accounts associated to single Customer use the following endpoint:

GET `{baseUrl}/account/list/{customerId}`

Where the `customerId` path variable is the identifier of the Customer.

The payload response looks like the following:
```json
{
	"_embedded": {
		"accountDTOList": [
			{
				"id": "06fe068e-1037-4079-825c-f1e05c269877",
				"version": 0,
				"balance": 10.50,
				"type": "CURRENT",
				"customer": {
					"id": "80baf275-2bda-4d50-81ac-8c06ebf0a7bd",
					"version": 0,
					"name": "Angel",
					"surname": "Gallegos",
					"balance": 10.50
				},
				"transactions": [
					{
						"id": "a7493c7e-614b-4b0a-affa-6512519eb5de",
						"version": 0,
						"amount": 10.00,
						"type": "TRANSFER"
					},
					{
						"id": "ebc8b1de-1f9d-4c6a-adc8-0876849e3f3d",
						"version": 0,
						"amount": 10.00,
						"type": "DEPOSIT"
					},
					{
						"id": "f0d394a5-3eb5-475e-956e-e46161f4d76f",
						"version": 0,
						"amount": 10.50,
						"type": "DEPOSIT"
					}
				],
				"_links": {
					"self": {
						"href": "http://localhost:8080/apicustomer/06fe068e-1037-4079-825c-f1e05c269877"
					}
				}
			}
		]
	},
	"_links": {
		"self": {
			"href": "http://localhost:8080/api/account/list/80baf275-2bda-4d50-81ac-8c06ebf0a7bd?page=0&size=20"
		}
	},
	"page": {
		"size": 20,
		"totalElements": 1,
		"totalPages": 1,
		"number": 0
	}
}
```

## List Accounts with Transactions of a Customer

To retrieve the information of the Customer along with the associated Accounts and Transactions of each Account, the next endpoint can be used:

GET `{baseUrl}/customer/{id}`

Where the `customerId` path variable is the identifier of the Customer.

The payload response looks like the following:
```json
{
	"id": "80baf275-2bda-4d50-81ac-8c06ebf0a7bd",
	"version": 0,
	"name": "Angel",
	"surname": "Gallegos",
	"balance": 10.50,
	"accounts": [
		{
			"id": "06fe068e-1037-4079-825c-f1e05c269877",
			"version": 0,
			"balance": 10.50,
			"type": "CURRENT",
			"transactions": [
				{
					"id": "a7493c7e-614b-4b0a-affa-6512519eb5de",
					"version": 0,
					"amount": 10.00,
					"type": "TRANSFER"
				},
				{
					"id": "ebc8b1de-1f9d-4c6a-adc8-0876849e3f3d",
					"version": 0,
					"amount": 10.00,
					"type": "DEPOSIT"
				},
				{
					"id": "f0d394a5-3eb5-475e-956e-e46161f4d76f",
					"version": 0,
					"amount": 10.50,
					"type": "DEPOSIT"
				}
			]
		}
	],
	"_links": {
		"self": {
			"href": "http://localhost:8080/apicustomer/80baf275-2bda-4d50-81ac-8c06ebf0a7bd"
		},
		"list": {
			"href": "http://localhost:8080/apicustomer/list"
		}
	}
}
```

### Note:

An insomnia collection(/banking-collection.json) is included with the relevant requests.