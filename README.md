# CurseX - AI-Powered Creative Social Content Generator

![CurseX Logo](https://img.shields.io/badge/CurseX-AI_Content_Generator-blue?style=for-the-badge&logo=ai&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-green?style=flat-square&logo=spring-boot)
![React](https://img.shields.io/badge/React-18.2.0-blue?style=flat-square&logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-5.0.0-blue?style=flat-square&logo=typescript)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange?style=flat-square&logo=mysql)

CurseX is an AI-powered creative writing platform that helps users generate viral, witty, chaotic, and engaging short-form content for platforms like X (Twitter), LinkedIn, Instagram, and Threads.

## ✨ Features

### 🎯 Core Features
- **AI-Powered Content Generation**: Uses Groq's Llama 3.1 model for fast, high-quality content
- **Multiple Input Modes**: Topic-based or draft refinement
- **Diverse Output Formats**: One-liners, threads, hot takes, lists, satirical posts, brand announcements, meta posts, meme captions
- **Tone Control**: Sarcastic, professional, chaotic, inspirational, aggressive, funny, dark humor, neutral
- **Platform Optimization**: Tailored content for Twitter, LinkedIn, Instagram, Threads, Reddit
- **Chaos Level Slider**: Adjustable randomness from 0 (safe) to 100 (unhinged)

### 🔐 Security & Authentication
- JWT-based authentication system
- Secure user registration and login
- Protected API endpoints
- Password encryption with BCrypt

### 🎨 User Experience
- Beautiful, responsive UI with glassmorphism design
- Real-time content generation with loading states
- Copy-to-clipboard functionality for generated content
- Error handling and validation
- Mobile-friendly responsive design

### 📊 Advanced Features
- Multiple content variations per generation
- Content history and management (planned)
- Brand voice presets (planned)
- Reply mode for social media interactions (planned)

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MySQL 8.0
- **Authentication**: JWT (JSON Web Tokens)
- **AI Integration**: Groq API (Llama 3.1-8B Instant)
- **Build Tool**: Maven
- **Documentation**: SpringDoc OpenAPI (Swagger)

### Frontend
- **Framework**: React 18.2.0
- **Language**: TypeScript 5.0.0
- **Build Tool**: Vite
- **Styling**: Custom CSS with modern design patterns
- **State Management**: React Hooks
- **HTTP Client**: Fetch API

### DevOps & Tools
- **Version Control**: Git
- **Containerization**: Docker (optional)
- **API Testing**: Postman/cURL
- **Code Quality**: ESLint, Prettier

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- **Java**: JDK 17 or higher
- **Node.js**: Version 18 or higher
- **MySQL**: Version 8.0 or higher
- **Maven**: Version 3.6 or higher
- **Git**: For cloning the repository

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/subhash-varun/cursx.git
cd cursx
```

### 2. Backend Setup

#### Configure MySQL Database
```sql
-- Create database
CREATE DATABASE cursx;

-- Create user (optional - you can use root)
CREATE USER 'cursx_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON cursx.* TO 'cursx_user'@'localhost';
FLUSH PRIVILEGES;
```

#### Configure Application Properties
Edit application.properties:
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/cursx
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JWT Configuration
jwt.secret=your_jwt_secret_key_here
jwt.expiration=86400000

# Groq AI Configuration
groq.api.key=your_groq_api_key_here
groq.api.url=https://api.groq.com/openai/v1/chat/completions

# Server Configuration
server.port=8080
```

#### Install Dependencies & Run Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

#### Install Dependencies
```bash
cd frontend
npm install
```

#### Run Frontend
```bash
npm run dev
```

The frontend will start on `http://localhost:5173`

## 📖 Usage

### 1. Access the Application
Open your browser and navigate to `http://localhost:5173`

### 2. Register/Login
- Create a new account or login with existing credentials
- JWT tokens are automatically managed and stored locally

### 3. Generate Content
1. **Select Mode**: Choose between "Topic" or "Draft"
2. **Choose Format**: Select desired output format (One Liner, Thread, Hot Take, etc.)
3. **Set Tone**: Pick the desired tone (Sarcastic, Professional, etc.)
4. **Select Platform**: Choose target platform (Twitter, LinkedIn, etc.)
5. **Adjust Chaos Level**: Use slider to control randomness (0-100)
6. **Enter Input**: Provide your topic or draft content
7. **Generate**: Click "Generate Content" button

### 4. Copy & Use
- Review generated variations
- Click the copy button (📋) on any variation
- Paste into your social media platform

## 🔧 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "username",
  "email": "user@example.com",
  "password": "password123"
}
```

#### Login User
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

### Content Generation Endpoint

#### Generate Content
```http
POST /api/generate
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "input": "AI replacing jobs",
  "mode": "TOPIC",
  "format": "HOT_TAKE",
  "tone": "SARCASTIC",
  "platform": "TWITTER",
  "chaos": 70
}
```

### Swagger Documentation
Access API documentation at: `http://localhost:8080/swagger-ui.html`

## 🐳 Docker Setup (Optional)

### Using Docker Compose
```yaml
# docker-compose.yml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: cursx
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/cursx
      GROQ_API_KEY: your_groq_api_key

  frontend:
    build: ./frontend
    ports:
      - "5173:5173"

volumes:
  mysql_data:
```

```bash
docker-compose up --build
```

## 🧪 Testing

### Backend Testing
```bash
cd backend
mvn test
```

### Frontend Testing
```bash
cd frontend
npm test
```

### Manual API Testing
```bash
# Register user
curl -X POST "http://localhost:8080/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"name":"testuser","email":"test@example.com","password":"password123"}'

# Login
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Generate content (replace TOKEN with actual JWT)
curl -X POST "http://localhost:8080/api/generate" \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"input":"AI in 2024","mode":"TOPIC","format":"ONE_LINER","tone":"SARCASTIC","platform":"TWITTER","chaos":50}'
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines
- Follow existing code style and conventions
- Add tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

## 📝 Roadmap

### Phase 1 (Current) ✅
- Basic authentication system
- Content generation with AI
- Multiple format and tone options
- Responsive UI

### Phase 2 (Upcoming)
- [ ] Content history and favorites
- [ ] Brand voice presets
- [ ] Reply mode for social media
- [ ] Content scheduling
- [ ] Analytics dashboard

### Phase 3 (Future)
- [ ] Team collaboration features
- [ ] Advanced AI models integration
- [ ] Mobile app
- [ ] API for third-party integrations

## 🐛 Troubleshooting

### Common Issues

**Backend won't start:**
- Check MySQL connection and credentials
- Ensure Java 17+ is installed
- Verify Groq API key is configured

**Frontend build fails:**
- Clear node_modules: `rm -rf node_modules && npm install`
- Check Node.js version (18+ required)

**Authentication issues:**
- Clear browser localStorage
- Check JWT token expiration
- Verify backend is running on port 8080

**AI generation fails:**
- Verify Groq API key is valid
- Check internet connection
- Review API rate limits

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- **Groq** for providing fast AI inference
- **Spring Boot** for the robust backend framework
- **React** for the excellent frontend library
- **Open source community** for amazing tools and libraries

*Transform your ideas into viral content with AI-powered creativity!* 🚀
