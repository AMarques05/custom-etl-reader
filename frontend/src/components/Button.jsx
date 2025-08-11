export default function Button(props) {
	return (
		<button
			type="button"
			className={
				"inline-flex items-center justify-center gap-1 px-4 py-2 rounded-md " +
				"bg-gray-600 text-white hover:bg-blue-400 active:bg-gray-600/90 " +
				"border border-gray-500 shadow-sm " +
				"focus:outline-none focus:ring-2 focus:ring-cyan-400 focus:ring-offset-0 " +
				"disabled:opacity-50 disabled:cursor-not-allowed " +
                "w-full transform transition-all duration-200"
			}
			onClick={props.onClick}
		>
            <span className="text-sm font-medium">Upload</span>
		</button>
	);
}
