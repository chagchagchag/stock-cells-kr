import { createStore } from 'redux';

export default createStore(function(state, action){
	if(state == undefined){
		return { selected_ticker: '' };
	}
	if(action.type === 'SEARCH/TICKER_CHANGE'){
		return {...state, selected_ticker: state.selected_ticker};
	}
}, window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__());