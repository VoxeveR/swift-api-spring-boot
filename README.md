
# Swift API

The project involves developing an application to parse, store, and manage SWIFT codes (Bank Identifier Codes, BIC) in a database. The SWIFT codes are being parsed from a provided file, following specific rules, such as identifying headquarters and branches based on the code structure. This application stores this data in a PostgreSQL database. The project is containerized, the endpoints are accessible via localhost:8080.


## Run Locally

Clone the project

```bash
  git clone https://github.com/VoxeveR/SWIFT_API.git
```

Go to the project build directory

```bash
  cd build
```

Build using docker (docker installation - https://docs.docker.com/engine/install/)

```bash
  docker build -t springapi .
```

Run using docker compose 

```bash
  docker compose up
```





__*NOTE: Database is being created and dropped at each re-launch of program. It can be changed in application.properties.*__
