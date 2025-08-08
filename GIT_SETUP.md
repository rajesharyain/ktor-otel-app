# Git Setup Guide for Ktor OpenTelemetry Application

## ✅ Local Git Repository Status

Your local Git repository has been successfully initialized with:
- **Repository**: `ktor-otel-app`
- **Initial Commit**: "Initial commit: Ktor OpenTelemetry Application with Jaeger integration"
- **Files Committed**: 21 files (1,427 lines of code)

## 🚀 Connecting to Remote Repository

### Option 1: GitHub

1. **Create a new repository on GitHub:**
   - Go to https://github.com/new
   - Repository name: `ktor-otel-app`
   - Description: "Full-stack Kotlin application with Ktor 3, OpenTelemetry, and Jaeger"
   - Choose Public or Private
   - **Don't** initialize with README, .gitignore, or license (we already have these)

2. **Connect your local repository:**
   ```bash
   git remote add origin https://github.com/YOUR_USERNAME/ktor-otel-app.git
   git branch -M main
   git push -u origin main
   ```

### Option 2: GitLab

1. **Create a new project on GitLab:**
   - Go to https://gitlab.com/projects/new
   - Project name: `ktor-otel-app`
   - Description: "Full-stack Kotlin application with Ktor 3, OpenTelemetry, and Jaeger"
   - Choose Public or Private
   - **Don't** initialize with README

2. **Connect your local repository:**
   ```bash
   git remote add origin https://gitlab.com/YOUR_USERNAME/ktor-otel-app.git
   git branch -M main
   git push -u origin main
   ```

### Option 3: Bitbucket

1. **Create a new repository on Bitbucket:**
   - Go to https://bitbucket.org/repo/create
   - Repository name: `ktor-otel-app`
   - Description: "Full-stack Kotlin application with Ktor 3, OpenTelemetry, and Jaeger"
   - Choose Public or Private
   - **Don't** initialize with README

2. **Connect your local repository:**
   ```bash
   git remote add origin https://bitbucket.org/YOUR_USERNAME/ktor-otel-app.git
   git branch -M main
   git push -u origin main
   ```

## 🔧 Git Configuration (if not already set)

Before pushing, make sure your Git identity is configured:

```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

## 📋 Useful Git Commands

### Check Status
```bash
git status
```

### View Commit History
```bash
git log --oneline
```

### Add Changes
```bash
git add .                    # Add all changes
git add specific-file.kt     # Add specific file
```

### Commit Changes
```bash
git commit -m "Your commit message"
```

### Push Changes
```bash
git push origin main
```

### Pull Changes
```bash
git pull origin main
```

### Create and Switch Branches
```bash
git checkout -b feature/new-feature
git checkout main
```

### View Remote Repositories
```bash
git remote -v
```

## 🏷️ Recommended Branching Strategy

### Main Branch
- `main` - Production-ready code
- `develop` - Development branch (optional)

### Feature Branches
- `feature/user-management` - New user features
- `feature/product-catalog` - Product-related features
- `feature/observability` - Monitoring improvements

### Example Workflow
```bash
# Create feature branch
git checkout -b feature/new-api-endpoint

# Make changes and commit
git add .
git commit -m "Add new API endpoint for analytics"

# Push feature branch
git push origin feature/new-api-endpoint

# Create Pull Request on GitHub/GitLab/Bitbucket
# After review, merge to main
```

## 🔒 Security Considerations

### Environment Variables
Never commit sensitive information like:
- API keys
- Database passwords
- OAuth secrets

### .env Files
Create a `.env.example` file with placeholder values:
```bash
# .env.example
DATABASE_URL=postgresql://user:password@localhost:5432/dbname
API_KEY=your_api_key_here
```

### Update .gitignore
Make sure `.env` files are in your `.gitignore`:
```gitignore
# Environment variables
.env
.env.local
.env.production
```

## 📝 Commit Message Guidelines

Use conventional commit messages:
```
feat: add user authentication
fix: resolve OpenTelemetry connection issue
docs: update README with new endpoints
refactor: improve error handling in routes
test: add unit tests for user service
```

## 🚀 CI/CD Integration

### GitHub Actions Example
Create `.github/workflows/ci.yml`:
```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Build with Gradle
      run: ./gradlew build
    - name: Run tests
      run: ./gradlew test
```

## 📊 Repository Statistics

Your repository contains:
- **21 files** committed
- **1,427 lines** of code
- **Complete application** with:
  - Ktor 3 framework
  - OpenTelemetry integration
  - Jaeger tracing
  - Docker Compose setup
  - Comprehensive documentation

## 🎯 Next Steps

1. **Choose a remote repository** (GitHub/GitLab/Bitbucket)
2. **Follow the connection steps** above
3. **Set up CI/CD** for automated testing
4. **Create feature branches** for new development
5. **Invite collaborators** if working in a team

## 🆘 Troubleshooting

### Authentication Issues
```bash
# Use Personal Access Token (recommended)
git remote set-url origin https://YOUR_TOKEN@github.com/YOUR_USERNAME/ktor-otel-app.git

# Or use SSH
git remote set-url origin git@github.com:YOUR_USERNAME/ktor-otel-app.git
```

### Large File Issues
If you encounter large file issues:
```bash
# Check for large files
git rev-list --objects --all | git cat-file --batch-check='%(objecttype) %(objectname) %(objectsize) %(rest)' | sed -n 's/^blob //p' | sort --numeric-sort --key=2 | tail -10
```

### Reset Repository
If you need to start over:
```bash
rm -rf .git
git init
git add .
git commit -m "Initial commit"
```

---

**Your Ktor OpenTelemetry application is now ready for collaborative development! 🚀** 