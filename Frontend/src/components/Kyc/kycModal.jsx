import React, { useState } from 'react';
import Button from '../button'; // or from './ui/Button'
import Modal from '../modal';   // or from './ui/Modal'
import { useSelector } from 'react-redux';
import { uploadKycDocument } from '../../api/customerApi';

const documentTypes = ['Aadhar Card', 'PAN Card', 'Passport'];

const KycModal = ({ isOpen, onClose, onUploadSuccess }) => {
  const [selectedType, setSelectedType] = useState(documentTypes[0]);
  const [file, setFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState('');
  const user = useSelector(state => state.user);

  const handleFileChange = e => {
    setFile(e.target.files[0]);
    setError('');
  };

  const typeConverter = {
    "Aadhar Card": "AADHAAR",
    "PAN Card": "PAN",
    "Passport": "PASSPORT"
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!file) {
      setError('Please select a file.');
      return;
    }
    setUploading(true);
    try {
      const formData = new FormData();
      formData.append('customerId', parseInt(user.userId));
      formData.append('documentType', typeConverter[selectedType]);
      formData.append('filePath', file.name);
      formData.append('data', file);

      console.log('Uploading KYC document:', ...formData)
      
      uploadKycDocument(formData)
        .then(response => {
          console.log('KYC upload response:', response);
          // onUploadSuccess();
          handleModalClose();
        })
        .catch(err => {
          console.error('KYC upload error:', err);
          setError('Upload failed. Please try again.');
        });

    } catch (err) {
      setError('Failed to upload. Please try again.');
    } finally {
      setUploading(false);
    }
  };

  // Reset modal on close
  const handleModalClose = () => {
    setFile(null);
    setSelectedType(documentTypes[0]);
    setError('');
    setUploading(false);
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={handleModalClose} title="Add New KYC Document">
      <form className="space-y-4" onSubmit={handleSubmit}>
        <div>
          <label className="block text-sm font-medium text-gray-700">Document Type</label>
          <select
            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            value={selectedType}
            onChange={e => setSelectedType(e.target.value)}
            required
          >
            {documentTypes.map(type => (
              <option key={type} value={type}>{type}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Upload PDF</label>
          <input
            type="file"
            accept=".pdf"
            className="mt-1 block w-full text-sm text-gray-700"
            onChange={handleFileChange}
            required
          />
        </div>
        {error && <div className="text-red-600 text-sm">{error}</div>}
        <div className="flex justify-end space-x-3 pt-4">
          <Button
            variant="secondary"
            type="button"
            onClick={handleModalClose}
            disabled={uploading}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            className="bg-blue-600 hover:bg-blue-700"
            disabled={uploading}
          >
            {uploading ? 'Uploading...' : 'Add KYC'}
          </Button>
        </div>
      </form>
    </Modal>
  );
};

export default KycModal;