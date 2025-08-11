import {useMemo} from "react";

function Table(props) {
    
    const columns = useMemo(() => {
        const set = new Set();
        for (const row of props.csvData) {
            for (const key in row) {
                set.add(key);
            }
        }
        return Array.from(set);
    }, [props.csvData]);

    if (!props.csvData || props.csvData.length === 0) {
        return <h1>No data available</h1>;
    }

    return (
        <div className="overflow-auto col-span-9 row-span-4 rounded shadow-xl/50 snap-y snap-mandatory">
            <table className="w-full border-collapse border">
                <thead>
                    <tr>
                        {columns.map((col) => (
                            <th key={col} className="bg-gray-200 text-left px-2 py-1 sticky top-0">{col}</th>
                    ))}
                </tr>
            </thead>
            <tbody>
                {props.csvData.map((row, rowIndex) => (
                    <tr key={rowIndex} className={`snap-start ${rowIndex % 2 === 0 ? "bg-gray-400" : "bg-gray-500"}`}>
                        {columns.map((col) => (
                            <td key={col} className="text-left px-2 py-1">{row[col]}</td>
                        ))}
                    </tr>
                ))}
            </tbody>
        </table>
    </div>
    );
}

export default Table;