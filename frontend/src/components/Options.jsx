
function Options(props) {

    if(props.csvData.length === 0) {
        return <div className="flex justify-center items-center rounded shadow-xl bg-gray-400 p-8 min-h-[200px]">
            <h1 className="text-xl">Upload CSV to see options</h1>
        </div>;
    }

    return (
        <div className="bg-gray-400 p-6 rounded shadow-xl min-h-[200px]">
            <h2 className="text-xl font-semibold mb-4">Processing Options</h2>
            {/* Options functionality will go here */}
        </div>
    );
}

export default Options;
