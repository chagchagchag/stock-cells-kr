import './theme/flatly-bootstrap.min.css';
// import './theme/darkly-bootstrap.min.css';

import './search/SearchInput';
import SearchInput from './search/SearchInput';

//-- react-bootstrap-table2
// import PriceCells from './demo/cells/react-bootstrap-table2/PriceCells';
// import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';

import PriceCells from './demo/cells/material-table/PriceCells';

function App() {
  return (
    <div className='container mt-5'>
      <SearchInput></SearchInput>
      <PriceCells></PriceCells>
    </div>
  );
}

export default App;
