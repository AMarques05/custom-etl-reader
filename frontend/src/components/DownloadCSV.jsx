import { useState } from "react";

function DownloadCSV(props) {
  const [isDownloading, setIsDownloading] = useState(false);

  const convertToCSV = (data) => {
    if (!data || data.length === 0) return '';
    
    const headers = Object.keys(data[0]);
    
    const csvHeaders = headers.join(',');
    
    const csvRows = data.map(row => {
      return headers.map(header => {
        const value = row[header];
        if (value === null || value === undefined) return '';
        const stringValue = String(value);
        if (stringValue.includes(',') || stringValue.includes('"') || stringValue.includes('\n')) {
          return `"${stringValue.replace(/"/g, '""')}"`;
        }
        return stringValue;
      }).join(',');
    });
    
    return [csvHeaders, ...csvRows].join('\n');
  };

  const downloadCSV = () => {
    setIsDownloading(true);
    
    try {
      const csvContent = convertToCSV(props.data);
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
      const link = document.createElement('a');
      
      if (link.download !== undefined) {
        const url = URL.createObjectURL(blob);
        link.setAttribute('href', url);
        link.setAttribute('download', props.filename || 'processed_data.csv');
        link.style.visibility = 'hidden';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
      }
    } catch (error) {
      console.error('Error downloading CSV:', error);
      alert('Error downloading CSV file');
    } finally {
      setIsDownloading(false);
    }
  };

  return (
    <div className="flex flex-col justify-center items-center gap-4 bg-gray-400 p-6 rounded shadow-xl">
        <button
            onClick={downloadCSV}
            disabled={!props.data || props.data.length === 0 || isDownloading}
            className="bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 text-white px-4 py-2 rounded transition-colors"
            >
            {isDownloading ? 'Downloading...' : 'Download as CSV'}
        </button>
    </div>
  );
}

export default DownloadCSV;