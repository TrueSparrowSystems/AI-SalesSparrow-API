# Application
export ENVIRONMENT='development'

# Database
export SPRING_BOOT_BE_DB_NAME='salessparrow_api'
export SPRING_BOOT_BE_DB_URL='jdbc:postgresql://postgres:5432/${SPRING_BOOT_BE_DB_NAME}' # For docker-compose
# export SPRING_BOOT_BE_DB_URL='jdbc:postgresql://localhost:5432/${SPRING_BOOT_BE_DB_NAME}' # For local
export SPRING_BOOT_BE_DB_USERNAME='postgres'
export SPRING_BOOT_BE_DB_PASSWORD='rootPassword'

# Authentication
export ENCRYPTION_KEY='1234567890'
export API_COOKIE_SECRET='1234567890'

# AWS
export AWS_ACCESS_KEY_ID=''
export AWS_SECRET_ACCESS_KEY=''
export AWS_REGION=''
export CACHE_CLUSTER_ID=''

# Database
export SPRING_BOOT_BE_DB_URL='http://localhost:8000'