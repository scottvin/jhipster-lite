import './App.css';

import JHipsterLiteNeonBlue from '@assets/JHipster-Lite-neon-blue.png';
import ReactLogo from '@assets/ReactLogo.png';

function App() {
  return (
    <div>
      <div id="app">
        <img alt="React logo" src={ReactLogo} />
        <br />
        <img alt="JHipster logo" src={JHipsterLiteNeonBlue} />
        <h1>React + TypeScript + Vite</h1>
        <p>
          Recommended IDE setup:&nbsp;
          <a href="https://code.visualstudio.com/" target="_blank" rel="noopener noreferrer">
            VSCode
          </a>
        </p>

        <p>
          <a href="https://vitejs.dev/guide/features.html" target="_blank" rel="noopener noreferrer">
            Vite Documentation
          </a>
          <span>&nbsp;|&nbsp;</span>
          <a href="https://reactjs.org/docs/" target="_blank" rel="noopener noreferrer">
            React Documentation
          </a>
        </p>

        <p>
          Edit&nbsp;<code>src/main/webapp/app/common/primary/app/App.tsx</code> to test hot module replacement.
        </p>
      </div>
    </div>
  );
}

export default App;
