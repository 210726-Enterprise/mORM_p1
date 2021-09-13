# Minimal ORM
This ORM requires that any models to be persisted on the database have a table already on the database as there is currently no method to dynamically create one.

In addition: Models must be identified with the "Entity" annotation.

Models must have an integer primary key, identified with the "Primary" annotation.

Models must have their non-primary fields identified with the "Column" annotation.

Models must follow the getMethod()/setMethod naming convention for all getters and setters

The ORM must be initialized for each model type through the use of the Configuration object's addAnnotatedClass() method before it can be used.

The ORM is capable of 4 methods: insertRow will insert rows to the appropriate database table

selectRow will select the indicated row from the appropriate database table

updateCell will update the indicated cell in the appropriate table, row, and column

deleteRow will delete the indicated row from the appropriate table
