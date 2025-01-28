@param String content
@param String fileName

<div class="bg-gray-50 rounded-lg border border-gray-200 p-4">
    <div class="relative">
        <textarea id="result"
                  class="w-full h-96 p-4 font-mono text-sm bg-white rounded-lg border border-gray-200 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 resize-none"
                  readonly>${content}</textarea>

        <button onclick="copyContent()"
                class="mt-4 inline-flex items-center px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg transition-colors duration-200">
            Copy Content
        </button>

        <button onclick="downloadTextFile(`${fileName}`)"
                class="mt-4 inline-flex items-center px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-medium rounded-lg transition-colors duration-200">
            Download Content
        </button>
    </div>
</div>

<script>
    function copyContent() {
        const textarea = document.getElementById('result');
        textarea.select();
        document.execCommand('copy');

        // Show feedback
        const button = event.target;
        const originalText = button.textContent;
        button.textContent = 'Copied!';

        setTimeout(() => {
            button.textContent = originalText;
        }, 2000);
    }

    function downloadTextFile(fileName) {
        const textarea = document.getElementById('result');
        const textContent = textarea.value;
        const blob = new Blob([textContent], { type: 'text/plain' });
        const a = document.createElement('a');

        a.href = URL.createObjectURL(blob);
        a.download = fileName || 'result.txt';

        document.body.appendChild(a);
        a.click();

        document.body.removeChild(a);
        URL.revokeObjectURL(a.href);
    }
</script>
