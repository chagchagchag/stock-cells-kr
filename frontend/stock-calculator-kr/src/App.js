import './theme/flatly-bootstrap.min.css';
// import './theme/darkly-bootstrap.min.css';

import './search/SearchInput';
import SearchInput from './search/SearchInput';

function App() {
  return (
    <div className='container mt-5'>
      <SearchInput></SearchInput>
    </div>
  );
}

export default App;
