## Checkpoint 3

The objective of this checkpoint was read [100 data folders](/test/resources/checkpoint3/data/) that in each folder has an [analysis.xml](/test/resources/checkpoint3/data/001/analysis.xml), an [analysis.yaml](/test/resources/checkpoint3/data/001/analysis.yaml) and a [profiling.json](/test/resources/checkpoint3/data/001/profiling.json) and did the following:

- From `analysis.xml`: Extract the table `‘dynamic’` that is under the element `‘total’` and rename the columns by adding the suffix `“ (Dynamic)”`
- From `analysis.yaml`: The table `‘static’` that is under the element `‘total’` and rename the columns by adding the suffix `“ (Static)”`
- From `profiling.json`: The names and percentages of the three top functions, with column names `“name #1”`, `“% #1”`, `“name #2”`, etc
- The final table should have a line per execution (i.e. folder) and columns from the three tools, plus a column with the name of the folder
- Add two lines at the end, one with the sum and another one with the average for the columns that represent numbers


### Internal DSL

[Internal DSL](/test/pt/up/fe/els2023/InternalTest.java) can be checked in these tests for all checkpoints.

### External DSL


- [External DSL Grammatic](/xtext/DSL/org.xtext.example.tablefork/src/org/xtext/example/tablefork/TableFork.xtext)
    - **`+`:** Indicates the beggining of an operation
    - **`*`:** Indicates the end of an operation that have other operations inside of them and needed to know when it ends, e.g. load, select, merge 
    - **i.e.:** In example, if a `fields` operation occurs it don't need an `* fields` when it's done because fields only receives arguments, no other operations 
    - **Operations**
         - **+ slice NUM1 to NUM2:** applies slices from `NUM1` to `NUM2` column
         - **+ merge (loads) * merge (operations):** receives `loads` operations and, when it is done(`* merge`) it applies other operations on this result
         - **+ load FILENAME * load (operations):** loads `FILENAME` and apply variable `(operations)` on this table
         - **+ forEach (operations) * forEach:** apply for each in each `(operations)`
         - **+ select (type) (fields) (from) * select:** apply select operation and, if needed, the `type` or `fields` or `from` operations
         - **+ fields FIELDNAMES:** apply fields operation for `FIELDNAMES`
         - **+ from FIELDNAME (fields):** selects `FIELDNAME`
         - **+ type TYPENAME:** selects an type by `TYPENAME`
         - **+ rename FIELDNAME to NEWNAME:** renames `FIELDNAME` to `NEWFIELDNAME`
         - **+ unstack:** apply unstack operation
         - **+ max COLUMNAME:** select max value from `COLUMNAME`
         - **+ stack:** apply stack operation
         - **+ aggregate (aggregate names):** apply agreggate operation on `(aggregate names)`
         - **+ unravel:** apply unravel operation
         - **+ save FILENAME:** saves the table with a `FILENAME`   
- [External DSL Parser](/src/pt/up/fe/els2023/Parser.java)
    - The parser uses [TableInteraction](/src/pt/up/fe/els2023/internal/TableInteraction.java) to convert the commands in operations on tables
- [Tests for External DSL with parser](/test/pt/up/fe/els2023/ExternalTest.java)
- [Checkpoint 1](/test/resources/checkpoint1/config.tablefork), [Checkpoint 2](/test/resources/checkpoint2/config.tablefork) and [Checkpoint 3](/test/resources/checkpoint3/config.tablefork) configure files for External DSL

#### Operations

New operations are created in addition to the previous checkpoint:

- **Slice:** 
    - Selects columns from a table from a range

- **Aggregate:**
    - Gets all values from the columns and create a new row with an aggregate operation, e.g. SUM or AVERAGE

- **Stack:**
    - Transform each row in a table and insert it into the column with type `TableValue` that stores these tables

- **Unravel:**
    - Gets all values from a `TableValue` column and put all them in a single row rename the new columns with which table name this element came from
![unravel](/docs/images/unravel_operation.png)

- **For Each:** 
    - Applies some operations for each table in a group of tables
![for each](/docs/images/for_each.png)

- **Load | Glob:**
    - Now the `load operation` needs only the filename path

