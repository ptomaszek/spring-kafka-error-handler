- Produce 3 valid records: 
   
   `http://localhost:8080/api/okay`

- Produce 1 faulty record that can be handled by `ErrorHandler` (skipped), and 2 valid records: 

  `http://localhost:8080/api/invalid`

- Produce 1 faulty record that `ErrorHandler` fails to handle, and 2 valid records:

  `http://localhost:8080/api/invalid-fatal-recovery`
