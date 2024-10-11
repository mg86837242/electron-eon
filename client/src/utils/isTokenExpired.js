import { jwtDecode } from 'jwt-decode';

export default function isTokenExpired(token) {
  try {
    const decodedToken = jwtDecode(token);

    if (typeof decodedToken.exp === 'undefined') {
      return true;
    }

    const currentTime = Date.now() / 1000;
    const expirationTime = decodedToken.exp;
    const bufferSeconds = 60;

    return expirationTime < currentTime + bufferSeconds;
  } catch (error) {
    console.error('Error decoding token:', error);
    return false;
  }
}
