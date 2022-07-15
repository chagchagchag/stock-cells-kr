let nextSearchId = 0;

export const searchCompany = (list) => {
	// console.log("search.action.js ('SEARCH/SEARCH_COMPANY') >>> list = ", list);
	return {
		type: "SEARCH/SEARCH_COMPANY",
		id: nextSearchId++,
		searchResult: list		
	}
};

export const selectTicker = (_ticker, _companyName) => {
	console.log("search.action.js ('SEARCH/SELECT_TICKER') >>> _ticker = ", _ticker, " _companyName = ", _companyName);
	return {
		type: "SEARCH/SELECT_TICKER",
		ticker: _ticker,
		companyName: _companyName
	}
};

