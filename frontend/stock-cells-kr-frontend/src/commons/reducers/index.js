import { combineReducers } from "redux";
import searchData from "./search.reducers";
import priceData from "./price.reducers";

export default combineReducers({
	searchData,
	priceData
})