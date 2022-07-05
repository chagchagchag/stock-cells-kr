import React, { Component } from 'react';
import BootstrapTable from 'react-bootstrap-table-next';
import ToolkitProvider, {CSVExport} from 'react-bootstrap-table2-toolkit/dist/react-bootstrap-table2-toolkit';

const {ExportCSVButton} = CSVExport;
const columns = [
	{
		dataField: 'id',
		text: 'Product Id'
	},
	{
		dataField: 'name',
		text: 'Product Name'
	},
	{
		dataField: 'price',
		text: 'Product Price'
	}
];

const products = [
	{
		id: 1,
		name: '나마비루',
		price: 22
	},
	{
		id: 2,
		name: '에어조던',
		price: 300000
	}
];

class PriceCells extends Component {

	render() {
		return (
			<ToolkitProvider keyField="id"
							 data={products}
							 columns={columns}
							 exportCSV={{
								//  noAutoBOM: false,
								//  blobType: "text/csv;charset=utf-8,\uFEFF"
								onlyExportSelection: true,
								exportAll: true,
								blobType: "text/csv"
							 }}>

				{
					props => (
						<div>
							<ExportCSVButton { ...props.csvProps}>
								Export CSV!!!
							</ExportCSVButton>
							<hr/>
							<BootstrapTable { ...props.baseProps}/>
						</div>
					)
				}
			</ToolkitProvider>
			// <div>
			// 	<div id="toolbar" style={{margin:0}} class="select">
			// 		<select class="form-control">
			// 			<option value="">현재 페이지 Export</option>
			// 			<option value="all">모든 데이터 Export</option>
			// 			<option value="selected">선택된 행 Export</option>
			// 		</select>
			// 	</div>
			// 	<table  id="table" 
			// 			data-show-export="true" 
			// 			data-pagination="true"
			// 			data-side-pagination="server"
			// 			data-click-to-select="true"
			// 			data-toolbar="#toolbar"
			// 			data-show-toggle="true"
			// 			data-show-columns="true"
			// 			data-url="https://examples.wenzhixin.net.cn/examples/bootstrap_table/data">
			// 	</table>
			// </div>
		);
	}
}

export default PriceCells;