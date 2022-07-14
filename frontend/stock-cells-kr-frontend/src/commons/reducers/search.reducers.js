const searchData = (state = [], action) =>{
	switch(action.type){
		case "SEARCH/SEARCH_COMPANY":
			return [
				...state,
				{
					id: action.id,
					result: action.result
				}
			];

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