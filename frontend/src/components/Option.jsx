import React from 'react';
    
function Option(props){
    function handleClick() {
        props.onToggle(props.id);
    }

    return (
        <div className="p-4 bg-gray-300 border border-gray-400 rounded shadow hover:shadow-lg hover:scale-105 transition-all duration-200" onClick={handleClick}>
            <div className="flex justify-between items-start mb-2">
                <h3 className="font-semibold text-gray-800">{props.title}</h3>
                <button
                    onClick={handleClick}
                    className={`w-6 h-6 border-2 rounded-full flex items-center justify-center focus:outline-none transition-colors ${
                        props.selected 
                            ? 'border-blue-500 bg-blue-50' 
                            : 'border-gray-500 hover:border-blue-400'
                    }`}
                >
                    {props.selected && (
                        <span className="w-3 h-3 bg-blue-500 rounded-full"></span>
                    )}
                </button>
            </div>
            <p className="text-sm text-gray-600 leading-relaxed">{props.description}</p>
        </div>
    );
}

export default Option;