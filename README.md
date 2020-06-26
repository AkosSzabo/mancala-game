# MancalaService

## Building and running the application
### Building the artifact
`mnv clean package` from the project folder
### Running integration tests
`mvn clean verify -P int-test` from the project folder
### Running the service locally
After building the artifact or its available in the /target directory 
`java -jar -Dspring.profiles.active=swagger target/MancalaService-0.0.1-SNAPSHOT.war`
### Using the service
- Local URI: *http://localhost:5000/api/...*
- Local swagger UI: *http://localhost:5000/swagger-ui.html*
- AWS URI: *http://mancalagame-env.eba-k3pprsqt.eu-west-2.elasticbeanstalk.com/api/...*
- AWS swagger UI: *http://mancalagame-env.eba-k3pprsqt.eu-west-2.elasticbeanstalk.com/swagger-ui.html*

URI: `/api/game/start`  
Operation: GET  
Starting a new match  

URI: `/api/game/fetch`  
Operation: GET  
Passing the id parameter fetches the game state of the given id

URI: `/api/game/move`  
Operation: POST    
Providing a JSON payload the player makes a move    
Example 1, will make PLAYER1 take all stones from pit 1 (existing game in db):    
`{"matchId"  : 1,
 "player" : "PLAYER1",
"pitNumber" : 1}`

Example 2, will result in error as PLAYER2 is not the active player in match one:  
`{"matchId"  : 1,
 "player" : "PLAYER2",
"pitNumber" : 1}`

## Design decisions
- Using H2 db as a document store to simplify storing matches, see `import.sql` for preloaded data
- New match and move returns current state, it is just convenience
- Request is passed to the service and not converted to a dto, its best practice to do generally
- Integration tests using JSON matching, its an effective way of doing it, right now should address the prettify issue (right now unformatted JSON), also its exact match required which is not a problem bc the response will be exact match, there are test utils that do proper JSON node matching with ignore etc
- Immutability by Cloneable interface
## Issues - TODO
- GameBoard class might seem to contain to much logic but  it encapsulates the behaviour and topology of GameBoard, factory method might be taken out  
- In an ideal world calculateNewState wouldn't exist in the evaluator class














