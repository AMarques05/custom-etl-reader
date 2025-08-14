import Button from "./Button";

function Submit(props) {

    if(props.csvData.length === 0) {
        return (
            <div className="bg-gray-400 py-2 px-4 rounded shadow-xl">
                <p className="text-gray-600">No CSV data available for submission.</p>
            </div>
        );
    }

    return (
        <div className="bg-gray-400 py-2 px-4 rounded shadow-xl">
            <Button onClick={props.onSubmit} text="Submit" />
        </div>
    );
}

export default Submit;