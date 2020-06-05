# message-board
### Simple message board using event sourcing and cqrs
This is intended for learning event sourcing and CQRS using a simple message board, primarily to see how things change with scaling.
There are two endpoints allowed:
 - **POST message** command. This comprises of the username and message body. The username is a tag to identify the user - there are no users set up on the system or security for user access. The message bosy can contain any text and is limited to 1000 chars.
 - **GET messages** query. This returns the first 10 messages in reverse order of creation date, specifying both username and message body. Where possible, this should be the order they were sent in.
#### Test Conditions
1. When 2 post commands are received, they will have separate creation times which will guarantee a consistent order they will be retrieved in.
1. Between 2 successive post commands, all queries will return exactly the same list of messages in the same order
1. For queries taking place on either side of 1-9 post commands, if the former query had messages ABCDEFGHIJ, then the latter will have all new messages followed by, as many of up to 10 and in the same order, messages ABCDEFGHI.

*It does not matter how many threads or scaled applications are running, these conditions should always be met.*
#### Versions
##### Single app, transactional
*module **single-transactional***
This is a simple base version in which both command and query are in the same application and transactions enforce consistency in the database.
This is expected to produce worse-case performance.
##### Single app, event sourcing with transactional CQRS
*module **single-es-trans-cqrs***
This is a version which stores all commands via non-blocking events, so post should be fast. Query is stored in a single table via transactions
This is exapected to be signiifcantly quicker, especially when scaled up