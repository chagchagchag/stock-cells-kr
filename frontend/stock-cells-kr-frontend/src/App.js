// import './theme/flatly-bootstrap.min.css';
// import './theme/darkly-bootstrap.min.css';
import './theme/litera-bootstrap.min.css';
// import './theme/styles.css';

import './search/SearchInput';
import SearchInput from './search/SearchInput';

//-- react-bootstrap-table2
// import PriceCells from './demo/cells/react-bootstrap-table2/PriceCells';
// import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';

import BuyingPlanCell from './planner/buy/BuyingPlanCell';

function App() {
  return (
    <div className='container mt-5'>
      <SearchInput></SearchInput>
      <BuyingPlanCell style={{width: '100%', height: '100%'}}></BuyingPlanCell>
    </div>
  );
}

export default App;
