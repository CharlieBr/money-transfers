# Money Transfers
Simple web service for transferring money between accounts

## Requirements:
- maven
- java 11

## Running app

To run application build project with maven
```
> mvn clean install
```

and then start it with `java`

```
> java -jar target/money-transfers-1.0.jar 
```

Server will be listening at http://localhost:8080

## API 

### POST /transfers

Triggers money transfer between two specified existing accounts. 

Request body format:

```json
{
   "from": "id-of-source-account",
   "to": "id-of-destination-account",
   "amount": 123.02
}
```

Example request:

```
curl --request POST \
  --url http://localhost:8080/transfers \
  --header 'accepts: application/json' \
  --header 'content-type: application/json' \
  --data '{
	  "from": "98f4f8b4-d671-445f-b818-fd351812dcbe",
	  "to": "9d49932e-2ced-4054-8527-d991257e0913",
	  "amount": 9.08
  }'
```

## Accounts

Accounts are populated from [accounts.json](https://github.com/CharlieBr/money-transfers/blob/master/src/main/resources/accounts.json) 
file at the start of application and stored in memory.
