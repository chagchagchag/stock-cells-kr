const searchData = (state = [], action) =>{
	switch(action.type){
		case "SEARCH/SEARCH_COMPANY":
			console.log("reducer (search_company) >>> ", action.searchResult);
			// if(state.searchResult != null || state.searchResult != undefined){
			// 	state.searchResult = [];
			// }
			state.searchResult = [];
			state.searchResult = action.searchResult;
			return state;
			// return [
			// 	...state.searchResult,
			// 	{
			// 		id: action.id,
			// 		searchResult: action.searchResult
			// 	}
			// ];

		case "SEARCH/SELECT_TICKER":
			return {
				...state,
				ticker: action.ticker,
				companyName: action.companyName
			};
			
		default:
			return state;
	}
};

export default searchData;