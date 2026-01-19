import React, { useState, useEffect } from 'react';
import './App.css';

// Authentication interfaces
interface AuthResponse {
  token: string;
  user: {
    id: number;
    username: string;
    email: string;
  };
}

interface LoginRequest {
  email: string;
  password: string;
}

interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

interface GenerateRequest {
  input: string;
  mode: 'TOPIC' | 'DRAFT';
  format?: 'ONE_LINER' | 'THREAD' | 'HOT_TAKE' | 'LIST' | 'SATIRICAL_POST' | 'BRAND_ANNOUNCEMENT' | 'META_POST' | 'MEME_CAPTION';
  tone?: 'SARCASTIC' | 'PROFESSIONAL' | 'CHAOTIC' | 'INSPIRATIONAL' | 'AGGRESSIVE' | 'FUNNY' | 'DARK_HUMOR' | 'NEUTRAL';
  platform?: 'TWITTER' | 'LINKEDIN' | 'INSTAGRAM' | 'THREADS' | 'REDDIT';
  chaos?: number;
}

interface GenerateResponse {
  outputs: string[];
}

const App: React.FC = () => {
  // Authentication state
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [user, setUser] = useState<{ id: number; username: string; email: string } | null>(null);
  const [token, setToken] = useState<string>('');

  // UI state
  const [isLogin, setIsLogin] = useState<boolean>(true);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string>('');

  // Form state
  const [loginForm, setLoginForm] = useState<LoginRequest>({ email: '', password: '' });
  const [registerForm, setRegisterForm] = useState<RegisterRequest>({ username: '', email: '', password: '' });

  // Content generation state
  const [input, setInput] = useState<string>('');
  const [mode, setMode] = useState<'TOPIC' | 'DRAFT'>('TOPIC');
  const [format, setFormat] = useState<'ONE_LINER' | 'THREAD' | 'HOT_TAKE' | 'LIST' | 'SATIRICAL_POST' | 'BRAND_ANNOUNCEMENT' | 'META_POST' | 'MEME_CAPTION'>('ONE_LINER');
  const [tone, setTone] = useState<'SARCASTIC' | 'PROFESSIONAL' | 'CHAOTIC' | 'INSPIRATIONAL' | 'AGGRESSIVE' | 'FUNNY' | 'DARK_HUMOR' | 'NEUTRAL'>('SARCASTIC');
  const [platform, setPlatform] = useState<'TWITTER' | 'LINKEDIN' | 'INSTAGRAM' | 'THREADS' | 'REDDIT'>('TWITTER');
  const [chaos, setChaos] = useState<number>(50);
  const [generatedOutputs, setGeneratedOutputs] = useState<string[]>([]);
  const [generating, setGenerating] = useState<boolean>(false);
  const [copiedIndex, setCopiedIndex] = useState<number | null>(null);

  // Check for existing token on component mount
  useEffect(() => {
    const storedToken = localStorage.getItem('authToken');
    if (storedToken) {
      setToken(storedToken);
      setIsAuthenticated(true);
      // You could decode the token to get user info, but for now we'll assume it's valid
    }
  }, []);

  // Manage body class for layout fixes
  useEffect(() => {
    if (isAuthenticated) {
      document.body.classList.add('dashboard-active');
      document.body.classList.remove('auth-active');
    } else {
      document.body.classList.add('auth-active');
      document.body.classList.remove('dashboard-active');
    }

    // Cleanup on unmount
    return () => {
      document.body.classList.remove('dashboard-active', 'auth-active');
    };
  }, [isAuthenticated]);

  // Authentication functions
  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginForm),
      });

      if (response.ok) {
        const authResponse: AuthResponse = await response.json();
        setToken(authResponse.token);
        setUser(authResponse.user);
        setIsAuthenticated(true);
        localStorage.setItem('authToken', authResponse.token);
        setLoginForm({ email: '', password: '' });
      } else {
        const errorData = await response.json();
        setError(errorData.message || 'Login failed');
      }
    } catch (err) {
      setError('Network error. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          name: registerForm.username, // Map username to name for backend
          email: registerForm.email,
          password: registerForm.password,
        }),
      });

      if (response.ok) {
        const authResponse: AuthResponse = await response.json();
        setToken(authResponse.token);
        setUser(authResponse.user);
        setIsAuthenticated(true);
        localStorage.setItem('authToken', authResponse.token);
        setRegisterForm({ username: '', email: '', password: '' });
      } else {
        const errorData = await response.json();
        setError(errorData.message || 'Registration failed');
      }
    } catch (err) {
      setError('Network error. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    setIsAuthenticated(false);
    setUser(null);
    setToken('');
    localStorage.removeItem('authToken');
    setGeneratedOutputs([]);
    setInput('');
  };

  // Content generation function
  const handleGenerate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!input.trim()) return;

    setGenerating(true);
    setError('');

    try {
      const response = await fetch('http://localhost:8080/api/generate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({
          input,
          mode,
          format,
          tone,
          platform,
          chaos,
        }),
      });

      if (response.ok) {
        const generateResponse: GenerateResponse = await response.json();
        setGeneratedOutputs(generateResponse.outputs);
      } else if (response.status === 401) {
        setError('Authentication expired. Please login again.');
        handleLogout();
      } else {
        const errorData = await response.json();
        setError(errorData.message || 'Generation failed');
      }
    } catch (err) {
      setError('Network error. Please try again.');
    } finally {
      setGenerating(false);
    }
  };

  return (
    <div className={`app ${!isAuthenticated ? 'auth-mode' : 'dashboard-mode'}`}>
      <div className="background-gradient"></div>

      {!isAuthenticated ? (
        <div className="auth-container">
          <div className="auth-card">
            <div className="auth-header">
              <h1 className="app-title">CurseX</h1>
              <p className="app-subtitle">AI-Powered Content Generator</p>
            </div>

            <div className="auth-tabs">
              <button
                className={`tab-button ${isLogin ? 'active' : ''}`}
                onClick={() => {
                  setIsLogin(true);
                  setError('');
                }}
              >
                Sign In
              </button>
              <button
                className={`tab-button ${!isLogin ? 'active' : ''}`}
                onClick={() => {
                  setIsLogin(false);
                  setError('');
                }}
              >
                Sign Up
              </button>
            </div>

            {error && <div className="error-message">{error}</div>}

            {isLogin ? (
              <form onSubmit={handleLogin} className="auth-form">
                <div className="form-group">
                  <label htmlFor="login-email">Email</label>
                  <input
                    id="login-email"
                    type="email"
                    value={loginForm.email}
                    onChange={(e) => setLoginForm({ ...loginForm, email: e.target.value })}
                    required
                    placeholder="Enter your email"
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="login-password">Password</label>
                  <input
                    id="login-password"
                    type="password"
                    value={loginForm.password}
                    onChange={(e) => setLoginForm({ ...loginForm, password: e.target.value })}
                    required
                    placeholder="Enter your password"
                  />
                </div>
                <button type="submit" className="auth-button" disabled={loading}>
                  {loading ? 'Signing In...' : 'Sign In'}
                </button>
              </form>
            ) : (
              <form onSubmit={handleRegister} className="auth-form">
                <div className="form-group">
                  <label htmlFor="register-username">Username</label>
                  <input
                    id="register-username"
                    type="text"
                    value={registerForm.username}
                    onChange={(e) => setRegisterForm({ ...registerForm, username: e.target.value })}
                    required
                    placeholder="Choose a username"
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="register-email">Email</label>
                  <input
                    id="register-email"
                    type="email"
                    value={registerForm.email}
                    onChange={(e) => setRegisterForm({ ...registerForm, email: e.target.value })}
                    required
                    placeholder="Enter your email"
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="register-password">Password</label>
                  <input
                    id="register-password"
                    type="password"
                    value={registerForm.password}
                    onChange={(e) => setRegisterForm({ ...registerForm, password: e.target.value })}
                    required
                    placeholder="Create a password"
                  />
                </div>
                <button type="submit" className="auth-button" disabled={loading}>
                  {loading ? 'Creating Account...' : 'Sign Up'}
                </button>
              </form>
            )}
          </div>
        </div>
      ) : (
        <div className="dashboard">
          <header className="dashboard-header">
            <div className="header-content">
              <h1 className="dashboard-title">Welcome back, {user?.username}!</h1>
              <button onClick={handleLogout} className="logout-button">
                Logout
              </button>
            </div>
          </header>

          <main className="dashboard-main">
            <div className="generator-card">
              <h2 className="card-title">Generate Content</h2>
              <p className="card-subtitle">Enter a prompt to generate AI-powered content</p>

              {error && <div className="error-message">{error}</div>}

              <form onSubmit={handleGenerate} className="generator-form">
                <div className="form-row">
                  <div className="form-group">
                    <label htmlFor="mode">Mode</label>
                    <select
                      id="mode"
                      value={mode}
                      onChange={(e) => setMode(e.target.value as 'TOPIC' | 'DRAFT')}
                      className="mode-select"
                    >
                      <option value="TOPIC">Topic</option>
                      <option value="DRAFT">Draft</option>
                    </select>
                  </div>
                  <div className="form-group">
                    <label htmlFor="format">Format</label>
                    <select
                      id="format"
                      value={format}
                      onChange={(e) => setFormat(e.target.value as any)}
                      className="format-select"
                    >
                      <option value="ONE_LINER">One Liner</option>
                      <option value="THREAD">Thread</option>
                      <option value="HOT_TAKE">Hot Take</option>
                      <option value="LIST">List</option>
                      <option value="SATIRICAL_POST">Satirical Post</option>
                      <option value="BRAND_ANNOUNCEMENT">Brand Announcement</option>
                      <option value="META_POST">Meta Post</option>
                      <option value="MEME_CAPTION">Meme Caption</option>
                    </select>
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label htmlFor="tone">Tone</label>
                    <select
                      id="tone"
                      value={tone}
                      onChange={(e) => setTone(e.target.value as any)}
                      className="tone-select"
                    >
                      <option value="SARCASTIC">Sarcastic</option>
                      <option value="PROFESSIONAL">Professional</option>
                      <option value="CHAOTIC">Chaotic</option>
                      <option value="INSPIRATIONAL">Inspirational</option>
                      <option value="AGGRESSIVE">Aggressive</option>
                      <option value="FUNNY">Funny</option>
                      <option value="DARK_HUMOR">Dark Humor</option>
                      <option value="NEUTRAL">Neutral</option>
                    </select>
                  </div>
                  <div className="form-group">
                    <label htmlFor="platform">Platform</label>
                    <select
                      id="platform"
                      value={platform}
                      onChange={(e) => setPlatform(e.target.value as any)}
                      className="platform-select"
                    >
                      <option value="TWITTER">Twitter</option>
                      <option value="LINKEDIN">LinkedIn</option>
                      <option value="INSTAGRAM">Instagram</option>
                      <option value="THREADS">Threads</option>
                      <option value="REDDIT">Reddit</option>
                    </select>
                  </div>
                </div>
                <div className="form-group">
                  <label htmlFor="chaos">Chaos Level: {chaos}</label>
                  <input
                    id="chaos"
                    type="range"
                    min="0"
                    max="100"
                    value={chaos}
                    onChange={(e) => setChaos(parseInt(e.target.value))}
                    className="chaos-slider"
                  />
                  <div className="chaos-labels">
                    <span>Sane</span>
                    <span>Chaotic</span>
                  </div>
                </div>
                <div className="form-group">
                  <label htmlFor="input">Content Input</label>
                  <textarea
                    id="input"
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    placeholder="Describe what you want to generate..."
                    rows={4}
                    required
                  />
                </div>
                <button type="submit" className="generate-button" disabled={generating}>
                  {generating ? 'Generating...' : 'Generate Content'}
                </button>
              </form>

              {generatedOutputs.length > 0 && (
                <div className="generated-content">
                  <h3 className="content-title">Generated Content</h3>
                  <div className="outputs-grid">
                    {generatedOutputs.slice(1).map((output, index) => {
                      const originalIndex = index + 1;
                      return (
                        <div key={originalIndex} className="output-card">
                          <div className="output-header">
                            <span className="output-number">Variation {index + 1}</span>
                            <button
                              className={`copy-button ${copiedIndex === originalIndex ? 'copied' : ''}`}
                              onClick={async () => {
                                try {
                                  await navigator.clipboard.writeText(output);
                                  setCopiedIndex(originalIndex);
                                  setTimeout(() => setCopiedIndex(null), 2000);
                                } catch (err) {
                                  console.error('Failed to copy text: ', err);
                                }
                              }}
                              title={copiedIndex === originalIndex ? 'Copied!' : 'Copy to clipboard'}
                            >
                              {copiedIndex === originalIndex ? '✅' : '📋'}
                            </button>
                          </div>
                          <div className="output-content">
                            <pre>{output}</pre>
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </div>
              )}
            </div>
          </main>
        </div>
      )}
    </div>
  );
};

export default App;
