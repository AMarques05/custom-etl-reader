import {useState} from "react";

export default function Input(props) {
  const [fileName, setFileName] = useState("");

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setFileName(file.name);
      props.onChange(event);
    }
  };

  return (
      <label className="w-full cursor-pointer bg-gray-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded-lg">
        {fileName || "Upload File"}
        <input
          type="file"
          className="hidden"
          onChange={handleFileChange}
        />
      </label>
  );
}
