# OAuth2 Integration with GitHub & Google

A full-stack Spring Boot application with React frontend that implements OAuth2 authentication using Google and GitHub providers.

## Features

- 🔐 **OAuth2 Authentication** - Secure login with Google and GitHub
- 👤 **User Profile Management** - View and edit user profiles
- 🛡️ **Session-based Security** - Secure session management
- 📱 **Responsive Design** - Modern, mobile-friendly UI
- 🔄 **Real-time Updates** - Live profile updates

## Architecture

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.2.0
- **Security**: Spring Security with OAuth2 Client
- **Database**: MySQL with JPA/Hibernate
- **Session Management**: HTTP Session-based authentication

### Frontend (React)
- **Framework**: React 19.2.0
- **Routing**: React Router DOM
- **HTTP Client**: Axios
- **Styling**: Custom CSS with responsive design

### Database Schema

#### User Entity
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(500),
    bio TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
```

#### AuthProvider Entity
```sql
CREATE TABLE auth_providers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    provider ENUM('GOOGLE', 'GITHUB') NOT NULL,
    provider_user_id VARCHAR(255) NOT NULL,
    provider_email VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## Prerequisites

- Java 17 or higher
- Node.js 16 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Setup Instructions

### 1. Database Setup

1. Install MySQL and create a database:
```sql
CREATE DATABASE oauth2app;
```

2. Update database credentials in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. OAuth2 Provider Setup

#### Google OAuth2 Setup
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable Google+ API
4. Create OAuth2 credentials:
   - Application type: Web application
   - Authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`
5. Copy Client ID and Client Secret

#### GitHub OAuth2 Setup
1. Go to [GitHub Developer Settings](https://github.com/settings/developers)
2. Create a new OAuth App:
   - Authorization callback URL: `http://localhost:8080/login/oauth2/code/github`
3. Copy Client ID and Client Secret

### 3. Environment Variables

Set the following environment variables or update `application.properties`:

```bash
export GOOGLE_CLIENT_ID=your_google_client_id
export GOOGLE_CLIENT_SECRET=your_google_client_secret
export GITHUB_CLIENT_ID=your_github_client_id
export GITHUB_CLIENT_SECRET=your_github_client_secret
```

### 4. Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Install dependencies and run:
```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 5. Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The frontend will start on `http://localhost:3000`

## API Endpoints

### Authentication
- `GET /oauth2/authorization/google` - Initiate Google OAuth2 login
- `GET /oauth2/authorization/github` - Initiate GitHub OAuth2 login
- `POST /api/logout` - Logout user

### User Management
- `GET /api/user` - Get current user information
- `POST /api/profile` - Update user profile

## Usage

1. **Home Page**: Visit `http://localhost:3000` to see the landing page
2. **Login**: Click "Continue with Google" or "Continue with GitHub" to authenticate
3. **Profile**: After successful login, you'll be redirected to your profile page
4. **Edit Profile**: Click "Edit Profile" to update your display name and bio
5. **Logout**: Click "Logout" to end your session

## Security Features

- **CSRF Protection**: Disabled for API endpoints (can be enabled for production)
- **CORS Configuration**: Configured for frontend-backend communication
- **Session Management**: Secure HTTP session handling
- **OAuth2 Integration**: Secure authentication with Google and GitHub

## Project Structure

```
oauth2-github-google-app/
├── backend/
│   ├── src/main/java/com/example/oauth2app/
│   │   ├── controller/          # REST controllers
│   │   ├── entity/             # JPA entities
│   │   ├── repository/         # Data repositories
│   │   ├── security/           # Security configuration
│   │   └── service/           # Business logic
│   └── src/main/resources/
│       └── application.properties
└── frontend/
    ├── src/
    │   ├── components/        # React components
    │   ├── contexts/          # React contexts
    │   └── App.js            # Main app component
    └── package.json
```

## Development

### Running Tests
```bash
# Backend tests
cd backend
mvn test

# Frontend tests
cd frontend
npm test
```

### Building for Production
```bash
# Backend
cd backend
mvn clean package

# Frontend
cd frontend
npm run build
```

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Ensure MySQL is running
   - Check database credentials in `application.properties`

2. **OAuth2 Authentication Fails**
   - Verify client IDs and secrets are correct
   - Check redirect URIs match exactly

3. **CORS Issues**
   - Ensure frontend is running on `http://localhost:3000`
   - Check CORS configuration in `SecurityConfig.java`

4. **Session Issues**
   - Clear browser cookies and try again
   - Check session timeout configuration

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request



