import {useMemo, useState} from "react";

function ProcessedDataTable(props) {
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const [isDarkMode, setIsDarkMode] = useState(false);
    
    const columns = useMemo(() => {
        const set = new Set();
        for (const row of props.processedData) {
            for (const key in row) {
                set.add(key);
            }
        }
        return Array.from(set);
    }, [props.processedData]);

    const totalItems = props.processedData.length;
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const currentData = props.processedData.slice(startIndex, endIndex);

    useMemo(() => {
        setCurrentPage(1);
    }, [props.processedData]);

    const handlePageChange = (page) => {
        setCurrentPage(page);
    };

    const handleItemsPerPageChange = (e) => {
        setItemsPerPage(Number(e.target.value));
        setCurrentPage(1);
    };

    const toggleDarkMode = () => {
        setIsDarkMode(!isDarkMode);
    };

    if (!props.processedData || props.processedData.length === 0) {
        return (
            <div className={`flex items-center justify-center rounded shadow-xl p-8 min-h-[300px] ${isDarkMode ? 'bg-gray-800 text-white' : 'bg-gray-400 text-black'}`}>
                No processed data to display.
            </div>
        );
    }

    return (
        <div className={`rounded shadow-xl p-4 relative ${isDarkMode ? 'bg-gray-800' : 'bg-gray-400'}`}>
            {/* Title */}
            <div className="mb-4">
                <h2 className={`text-2xl font-bold ${isDarkMode ? 'text-white' : 'text-black'}`}>
                    Processed Data Results
                </h2>
                <p className={`text-sm ${isDarkMode ? 'text-gray-300' : 'text-gray-700'}`}>
                    Your data has been successfully processed and cleaned
                </p>
            </div>

            {/* Items per page selector */}
            <div className="flex justify-between items-center mb-4">
                <div className="flex items-center gap-2">
                    <label className={`text-sm font-medium ${isDarkMode ? 'text-white' : 'text-black'}`}>Items per page:</label>
                    <select 
                        value={itemsPerPage} 
                        onChange={handleItemsPerPageChange}
                        className={`px-2 py-1 rounded border ${isDarkMode ? 'bg-gray-700 text-white border-gray-600' : 'bg-white text-black border-gray-300'}`}
                    >
                        <option value={5}>5</option>
                        <option value={10}>10</option>
                        <option value={25}>25</option>
                        <option value={50}>50</option>
                        <option value={100}>100</option>
                    </select>
                </div>
                <div className={`text-sm ${isDarkMode ? 'text-gray-300' : 'text-gray-700'}`}>
                    Showing {startIndex + 1}-{Math.min(endIndex, totalItems)} of {totalItems} items
                </div>
            </div>

            {/* Table */}
            <div className={`overflow-auto max-h-[400px] border rounded ${isDarkMode ? 'border-gray-600' : 'border-gray-300'}`}>
                <table className="w-full border-collapse">
                    <thead>
                        <tr>
                            {columns.map((col) => (
                                <th key={col} className={`text-left px-3 py-2 sticky top-0 border-b ${
                                    isDarkMode 
                                        ? 'bg-gray-700 text-white border-gray-600' 
                                        : 'bg-gray-200 text-black border-gray-300'
                                }`}>{col}</th>
                            ))}
                        </tr>
                    </thead>
                    <tbody>
                        {currentData.map((row, rowIndex) => (
                            <tr key={startIndex + rowIndex} className={`${
                                isDarkMode
                                    ? (startIndex + rowIndex) % 2 === 0 
                                        ? "bg-gray-800 hover:bg-gray-700" 
                                        : "bg-gray-900 hover:bg-gray-700"
                                    : (startIndex + rowIndex) % 2 === 0 
                                        ? "bg-white hover:bg-gray-50" 
                                        : "bg-gray-100 hover:bg-gray-50"
                            }`}>
                                {columns.map((col) => (
                                    <td key={col} className={`text-left px-3 py-2 border-b ${
                                        isDarkMode ? 'text-white border-gray-600' : 'text-black border-gray-300'
                                    }`}>{row[col]}</td>
                                ))}
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {/* Pagination controls */}
            <div className="flex justify-center items-center gap-2 mt-4">
                <button 
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 1}
                    className={`px-3 py-1 rounded disabled:opacity-50 disabled:cursor-not-allowed ${
                        isDarkMode
                            ? 'bg-gray-600 hover:bg-gray-500 text-white'
                            : 'bg-gray-300 hover:bg-gray-200 text-black'
                    }`}
                >
                    Previous
                </button>
                
                {/* Page numbers */}
                <div className="flex gap-1">
                    {Array.from({length: Math.min(5, totalPages)}, (_, i) => {
                        let pageNum;
                        if (totalPages <= 5) {
                            pageNum = i + 1;
                        } else if (currentPage <= 3) {
                            pageNum = i + 1;
                        } else if (currentPage > totalPages - 3) {
                            pageNum = totalPages - 4 + i;
                        } else {
                            pageNum = currentPage - 2 + i;
                        }
                        
                        return (
                            <button
                                key={pageNum}
                                onClick={() => handlePageChange(pageNum)}
                                className={`px-3 py-1 rounded ${
                                    currentPage === pageNum 
                                        ? isDarkMode
                                            ? 'bg-blue-600 text-white'
                                            : 'bg-gray-600 text-white'
                                        : isDarkMode
                                            ? 'bg-gray-600 hover:bg-gray-500 text-white'
                                            : 'bg-gray-300 hover:bg-gray-200 text-black'
                                }`}
                            >
                                {pageNum}
                            </button>
                        );
                    })}
                </div>
                
                <button 
                    onClick={() => handlePageChange(currentPage + 1)}
                    disabled={currentPage === totalPages}
                    className={`px-3 py-1 rounded disabled:opacity-50 disabled:cursor-not-allowed ${
                        isDarkMode
                            ? 'bg-gray-600 hover:bg-gray-500 text-white'
                            : 'bg-gray-300 hover:bg-gray-200 text-black'
                    }`}
                >
                    Next
                </button>
            </div>

            {/* Dark mode toggle - positioned in bottom right */}
            <button
                onClick={toggleDarkMode}
                className={`absolute bottom-4 right-4 p-2 rounded-full transition-colors duration-200 ${
                    isDarkMode 
                        ? 'bg-yellow-400 text-gray-900 hover:bg-yellow-300' 
                        : 'bg-gray-700 text-white hover:bg-gray-600'
                }`}
                title={isDarkMode ? 'Switch to Light Mode' : 'Switch to Dark Mode'}
            >
                {isDarkMode ? (
                    <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                        <path fillRule="evenodd" d="M10 2a1 1 0 011 1v1a1 1 0 11-2 0V3a1 1 0 011-1zm4 8a4 4 0 11-8 0 4 4 0 018 0zm-.464 4.95l.707.707a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414zm2.12-10.607a1 1 0 010 1.414l-.706.707a1 1 0 11-1.414-1.414l.707-.707a1 1 0 011.414 0zM17 11a1 1 0 100-2h-1a1 1 0 100 2h1zm-7 4a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zM5.05 6.464A1 1 0 106.465 5.05l-.708-.707a1 1 0 00-1.414 1.414l.707.707zm1.414 8.486l-.707.707a1 1 0 01-1.414-1.414l.707-.707a1 1 0 011.414 1.414zM4 11a1 1 0 100-2H3a1 1 0 000 2h1z" clipRule="evenodd" />
                    </svg>
                ) : (
                    <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z" />
                    </svg>
                )}
            </button>
        </div>
    );
}

export default ProcessedDataTable;
