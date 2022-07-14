const priceData = (state = {}, action) => {
	switch(action.type){
		case "PRICE/STOCK_PRICE_RESULT":
			return {
				...state,
			};
		default:
			return {
				...state
			}
	}
}

export default priceData;