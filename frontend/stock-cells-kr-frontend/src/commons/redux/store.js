import { createStore } from 'redux';

export default createStore(function(state, action){
	if(state == undefined){
		return { selected_ticker: '' };
	}
	if(action.type === 'SEARCH/SELECTED_TICKER'){
		return {...state, selected_ticker: state.selected_ticker};
	}
	if(action.type === 'SEARCH/COMPANY_SEARCH_RESULT'){
		return {...state, companySearchResult: state.companySearchResult}
	}
	if(action.type === 'PRICE/STOCK_PRICE_RESULT'){
		return {...state, stockPriceResult: state.stockPriceResult}
	}
}, window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__());