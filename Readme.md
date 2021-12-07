# gfuweb -  "Gestione Finanziamenti Urbanistici" web application 

# Description
 **gfuweb** the application allows the management of urban financing requests received from municipalities. Its process consists of:
uploading the data of the Municipality or the Associations of Municipalities,
insertion of the list of law measures (Provvedimenti) for which grants are requested
management of the grants until they are disbursed.

The application also allows the management of various decoding tables and the management of the users' profile.

# Getting Started
The minimal operations needed to setup your own  gfuweb installation are the following
`

$ git clone https://github.com/csipiemonte/gfuweb

$ cd gfuweb

Under the docs/db folder you will find the scripts for the creation of the database schema gfuweb relies on.

Execute them on your postgresql database.

$ edit the standalone.xml of your application server according to you database information 

<datasource jndi-name="java:/gfuweb/jdbc/gfuwebDS" pool-name="gfuwebDS">
        <connection-url>jdbc:postgresql://<DB_hostname>:<DB_Port>/<DB_Name></connection-url>
        <driver>postgresql</driver>
        <security>
            <user-name><DB_user></user-name>
            <password><DB_passwd></password>
        </security>
</datasource>

$ mvn clean package -P....

# Prerequisites
- A [Postgresql](https://www.postgresql.org/) user/schema with CREATE/INSERT/UPDATE/DELETE privileges
- An instance of [Wildfly 17.0.1](https://www.wildfly.org/downloads/)
- A working Maven runtime

# Deployment
Just deploy the content of the tar file created under the target folder on your Wildfly instance/partition

# Usage

 <http_scheme>://<your_domain>/gfuweb
 
# Contributing
Please read CONTRIBUTING.md for details on our code of conduct, and the process for submitting pull requests to us.

# Versioning (Mandatory)
We use Semantic Versioning for versioning. (http://semver.org)

# Authors
See the list of contributors who participated in this project in file AUTHORS.txt.

# Copyrights
See the list of copyrighters in this project in file Copyrights.txt

# License
Licensed EUPL 1.2. See the LICENSE.txt file for details
