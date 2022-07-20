export const selectStockPriceData = (data) => {
	return {
		type: "PRICE/STOCK_PRICE_DATA",
		actionData: data
	}
}