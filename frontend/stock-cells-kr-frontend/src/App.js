// -- (start) bootstrap 테마 
// import './theme/flatly-bootstrap.min.css';
// import './theme/darkly-bootstrap.min.css';
import './theme/litera-bootstrap.min.css';
// import './theme/styles.css';

// -- (start) react-bootstrap
// 참고) https://react-bootstrap.github.io/getting-started/introduction/#css 
import 'bootstrap/dist/css/bootstrap.min.css';

import BuyPriceTable from './planner/buy/BuyPriceTable';
import SearchCompanyInput from './search/components/SearchCompanyInput';
import TickerList from './search/components/TickerList';

function App() {
  return (
    <div className='container mt-5'>
      <SearchCompanyInput></SearchCompanyInput>
      <TickerList></TickerList>
      <BuyPriceTable></BuyPriceTable>

    </div>
  );
}

export default App;
