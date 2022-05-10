import React from 'react'
import { useTable } from 'react-table'


function Table({columns, data}) {
	const{
		getTableProps, getTableBodyProps, headerGroups, rows, prepareRow
	} = useTable({columns, data,})

	return (
		<table {...getTableProps()}>
			<thead>
				{headerGroups.map(headerGroup => (
					<tr {...headerGroup.getHeaderGroupProps()}>
						{headerGroup.headers.map(column => (
							<th {...column.getHeaderProps()}>
								{column.render('Header')}
							</th>
						))}
					</tr>
				))}
			</thead>
			<tbody {...getTableBodyProps()}>
				{rows.map((row, i) => {
					prepareRow(row)
					return (
						<tr {...row.getRowProps()}>
						  {row.cells.map(cell => {
							return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
						  })}
						</tr>
					  )
				})}
			</tbody>
		</table>
	)
}

const JustSampleTable = () => {

	const playerColumns = [
		{Header : 'player', accessor: 'playerName'},
		{Header : 'age', accessor: 'playerAge'},
		{Header : 'team', accessor: 'playerTeam'}
	]

	const playerData = [
		{playerName: 'Jordan', playerAge: 53, playerTeam: 'Bulls'},
		{playerName: 'Iverson', playerAge: 39, playerTeam: 'Sixers'},
		{playerName: 'Mbape', playerAge: 21, playerTeam: 'Paris'}
	]

	return (
		<div>
			<Table columns={playerColumns} data={playerData}/>
		</div>
	);
};

export default JustSampleTable;