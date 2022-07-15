const searchData = (state = [], action) =>{
	switch(action.type){
		case "SEARCH/SEARCH_COMPANY":
			state.searchResult = [];
			state.searchResult = action.searchResult;
			return state;

		case "SEARCH/SELECT_TICKER":
			state.searchResult = [];
			return {
				...state,
				selectedCompany : {
					ticker: action.ticker,
					companyName: action.companyName
				}
			};
			
		default:
			return state;
	}
};

export default searchData;