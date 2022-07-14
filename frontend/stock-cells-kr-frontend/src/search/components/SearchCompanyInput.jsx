import React, { useState } from 'react';
import { searchCompany } from '../../commons/actions/search.actions';
import { connect } from 'react-redux';

const SearchCompanyInput = ({dispatch}) => {
	var inputTagText = useState("searchText");
	var searchText = inputTagText[0];
	var setSearchText = inputTagText[1];

	// var searchResultProps = useState("searchResult");
	// var searchResult = searchResultProps[0];
	// var setSearchResult = searchResultProps[1];

	var handleChange = (e) => {
		if(e.target.value.trim() === ''){
			setSearchText('종목명 입력');
			return;
		}
		if(e.target.value.length === 0) return;

		setSearchText(e.target.value.trim());

		fetch('/ticker/stock?companyName='+searchText)
			.then(function(result){
				return result.json(); 
			})
			.then(function(json){
				console.log("검색어 : ", searchText, " 서버응답 : ", json);
				dispatch(searchCompany(json));
				// setSearchResult({searchResult: json});
			}.bind(this));
	}

	return (
		<div className="row">
			<div className="col-md-6 m-auto">
				<input 
					type="text" 
					id="search"
					className="form-cotrol form-control-lg"
					onKeyPress={function(e){
						handleChange(e);
					}.bind(this)}/>
			</div>
		</div>
	  );
};

// export default SearchCompanyInput;
export default connect()(SearchCompanyInput);