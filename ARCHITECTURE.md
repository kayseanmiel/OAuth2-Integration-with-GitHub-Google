# Architecture Diagram

## System Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React         │    │   Spring Boot   │    │   MySQL         │
│   Frontend      │    │   Backend       │    │   Database      │
│   (Port 3000)   │    │   (Port 8080)   │    │   (Port 3306)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │ HTTP/REST API         │ JDBC/JPA              │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────────────────────────────────────────────────────┐
│                    OAuth2 Providers                            │
│  ┌─────────────────┐              ┌─────────────────┐         │
│  │     Google       │              │     GitHub      │         │
│  │   OAuth2 API    │              │   OAuth2 API    │         │
│  └─────────────────┘              └─────────────────┘         │
└─────────────────────────────────────────────────────────────────┘
```

## Authentication Flow

```
1. User clicks "Login with Google/GitHub"
2. Frontend redirects to Spring Boot OAuth2 endpoint
3. Spring Boot redirects to OAuth2 provider
4. User authenticates with provider
5. Provider redirects back to Spring Boot with authorization code
6. Spring Boot exchanges code for access token
7. Spring Boot fetches user info from provider
8. Spring Boot creates/updates user in database
9. Spring Boot stores user in session
10. Spring Boot redirects to React frontend profile page
```

## Component Structure

### Backend Components
- **SecurityConfig**: OAuth2 and CORS configuration
- **OAuth2AuthenticationSuccessHandler**: Handles successful OAuth2 authentication
- **UserController**: REST API endpoints for user management
- **UserService**: Business logic for user operations
- **UserRepository**: Data access layer for users
- **AuthProviderRepository**: Data access layer for OAuth providers

### Frontend Components
- **AuthContext**: Authentication state management
- **Home**: Landing page with login buttons
- **Login**: Login page component
- **Profile**: User profile management
- **ProtectedRoute**: Route protection wrapper

## Data Flow

```
User Request → React Frontend → Spring Boot API → MySQL Database
                ↓
            OAuth2 Provider (Google/GitHub)
                ↓
            User Authentication
                ↓
            Session Management
                ↓
            Profile Data Display/Update
```
