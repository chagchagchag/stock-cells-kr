// -- (start) bootstrap 테마 
// import './theme/flatly-bootstrap.min.css';
// import './theme/darkly-bootstrap.min.css';
import './theme/litera-bootstrap.min.css';
// import './theme/styles.css';

// -- (start) react-bootstrap
// 참고) https://react-bootstrap.github.io/getting-started/introduction/#css 
import 'bootstrap/dist/css/bootstrap.min.css';

import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import BuyPriceTablePage from './planner/buy/page/BuyPriceTablePage';
import NavBarElement from './commons/navbar/NavBarElement';
import Intro from './intro/Intro';

function App() {
  return (
    <Router>
      <NavBarElement/>
      <Routes>
        <Route path="/" element={<Intro/>}/>
        <Route path="/buy-price-table" element={<BuyPriceTablePage/>}/>
      </Routes>
    </Router>
  );
}

export default App;
