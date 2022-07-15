import React, {Fragment, useState} from 'react'
import TickerSearchItem from './TickerSearchItem';

function TickerSearchListItem(props) {
	// subscribe = 
	// store = store.companySearchResult, 
	// type = "SEARCH/COMPANY_SEARCH_RESULT"

	var renderTickerSearchItem = (list) => {
		// console.log("props.companyResult = ", props.companySearchResult);
		// console.log(">>>>>>>>>> ", list);
		if(list.length == 0) return [];

		var componentList = [];

		for(var i=0; i<list.length; i++){
			var tickerItem = list[i];
			componentList.push(
				<TickerSearchItem
					key={tickerItem.ticker} 
					ticker={tickerItem.ticker} 
					companyName={tickerItem.companyName}
				/>
			);
		}

		return componentList;
	};

	return (
		<Fragment>
			{renderTickerSearchItem(props.companySearchResult)}
		</Fragment>
	)
}

export default TickerSearchListItem