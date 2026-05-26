# Fashion Backend (fashion_be)

## Overview
This repository contains the **backend** services for the Fashion project. It provides RESTful APIs, authentication, and data persistence for the frontend application.

## Tech Stack
- **Node.js** (v20+) with **Express**
- **MongoDB** (or any relational DB you prefer)
- **JWT** based authentication
- **Docker** for containerisation (optional)

## Getting Started
1. **Clone the repository**
   ```bash
   git clone <repo-url>
   cd fashion_be
   ```
2. **Install dependencies**
   ```bash
   npm install
   ```
3. **Configure environment variables**
   Create a `.env` file based on `.env.example`:
   ```dotenv
   PORT=5000
   DB_URI=mongodb://localhost:27017/fashion
   JWT_SECRET=your_secret_key
   ```
4. **Run the server**
   ```bash
   npm run dev   # starts with nodemon
   # or
   npm start      # production mode
   ```

## API Documentation
- The API is documented with **Swagger**. After starting the server, visit `http://localhost:{PORT}/api-docs`.
- Common endpoints:
  - `GET /api/products` – List products
  - `POST /api/auth/login` – User login
  - `POST /api/auth/register` – Register new user

## Testing
```bash
npm test
```

## Docker
```bash
docker build -t fashion-be .
 docker run -p 5000:5000 fashion-be
```

## Contributing
1. Fork the repo
2. Create a feature branch (`git checkout -b feature/awesome-feature`)
3. Commit your changes and push (`git push origin feature/awesome-feature`)
4. Open a Pull Request

## License
This project is licensed under the MIT License.
