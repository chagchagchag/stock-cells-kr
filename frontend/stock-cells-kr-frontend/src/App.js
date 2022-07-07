// import './theme/flatly-bootstrap.min.css';
// import './theme/darkly-bootstrap.min.css';
import './theme/litera-bootstrap.min.css';
// import './theme/styles.css';

import './search/SearchInput';
import SearchInput from './search/SearchInput';

//-- react-bootstrap-table2
// import PriceCells from './demo/cells/react-bootstrap-table2/PriceCells';
// import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';

import BuyingPlanCellAgGrid from './planner/buy/BuyingPlanCellAgGrid';

function App() {
  return (
    <div className='container mt-5'>
      <SearchInput></SearchInput>
      <BuyingPlanCellAgGrid></BuyingPlanCellAgGrid>
    </div>
  );
}

export default App;
