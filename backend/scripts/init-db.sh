#!/bin/bash

# Initialize all databases
echo "Initializing databases..."

# Set database connection parameters
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-3306}"
DB_USER="${DB_USER:-root}"
DB_PASSWORD="${DB_PASSWORD:-password}"

# Function to execute SQL file
execute_sql() {
    local sql_file=$1
    echo "Executing $sql_file..."
    mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASSWORD" < "$sql_file"
    if [ $? -eq 0 ]; then
        echo "✓ $sql_file executed successfully"
    else
        echo "✗ Failed to execute $sql_file"
        exit 1
    fi
}

# Execute all SQL files
cd "$(dirname "$0")/sql"

execute_sql "account-service.sql"
execute_sql "station-service.sql"
execute_sql "session-service.sql"
execute_sql "billing-service.sql"
execute_sql "settlement-service.sql"
execute_sql "dr-vpp-service.sql"

echo "Database initialization completed!"
