import React, { Component } from 'react';

class SearchInput extends Component {
	state = {
		list: []
	};
	
	componentDidMount(){
		fetch('data/dump-stock-list.json')
		.then(function(result){
			return result.json(); 
		})
		.then(function(json){
			console.log(json);
			this.setState({list:json});
		}.bind(this));

	}

	render() {
		var searchResultEl = [];
		for(var i=0; i<this.state.list.length; i++){
			var each = this.state.list[i];
			searchResultEl.push(
				<div key={each.corpCode} className='card card-body mb-1'>
					<small>회사명 : {each.name} / 종목코드 : {each.corpCode}</small>
				</div>
			)
		}

		return (
		<div className="row">
			<div className="col-md-6 m-auto">
			<h3 className="text-center mb-3">
				<i className="fa-solid fa-car-tunnel"></i> STOCK CELLS
			</h3>
			<input 
				type="text" 
				id="search" 
				className="form-control form-control-lg" 
				placeholder="종목 검색"/>
			</div>
        	{/* <div id="match-list"></div> */}
			<div>
				{searchResultEl}
			</div>
		</div>
		);
	}
}

export default SearchInput;