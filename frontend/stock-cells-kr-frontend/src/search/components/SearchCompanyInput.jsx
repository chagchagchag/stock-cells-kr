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
		if(e.target.value === ''){
			setSearchText('종목명 입력');
			return;
		}

		setSearchText(e.target.value);

		fetch('/ticker/stock?companyName='+searchText)
			.then(function(result){
				return result.json(); 
			})
			.then(function(json){
				console.log(json);
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
					onChange={function(e){
						handleChange(e);
					}.bind(this)}/>
			</div>
		</div>
	  );
};

// export default SearchCompanyInput;
export default connect()(SearchCompanyInput);