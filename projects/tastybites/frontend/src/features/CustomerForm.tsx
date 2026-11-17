import React, { useState, useEffect } from "react";
import type { Customer } from "../types";

interface Props {
  onCustomerAdded: (data: Partial<Customer>) => void;
  editingCustomer?: Customer | null;
}

const CustomerForm: React.FC<Props> = ({ onCustomerAdded, editingCustomer }) => {
  const [formData, setFormData] = useState<Partial<Customer>>({});

  useEffect(() => {
    if (editingCustomer) {
      setFormData(editingCustomer);
    }
  }, [editingCustomer]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    let newValue: string | boolean = value;

    if (name === "matricNo") {
      newValue = value === "yes";
    } else if (type === "checkbox") {
      newValue = checked;
    }

    setFormData((prev) => ({
      ...prev,
      [name]: newValue,
    }));
  };

  const handleSubmit = () => {
    if (
      formData.firstName &&
      formData.surname &&
      formData.middleName &&
      formData.dateOfBirth &&
      formData.homeAddress &&
      formData.dateOfRegistration &&
      formData.matricNo !== undefined
    ) {
      onCustomerAdded(formData);
      setFormData({});
    } else {
      console.warn("Please fill in all required fields.");
    }
  };

  return (
    <div className="p-4 bg-orange-100">
      <h3 className="text-lg font-bold text-orange-600">
        {editingCustomer ? "Edit Customer" : "Add Customer"}
      </h3>

      <input
        type="text"
        name="firstName"
        placeholder="First Name"
        value={formData.firstName || ""}
        onChange={handleInputChange}
      />
      <input
        type="text"
        name="middleName"
        placeholder="Middle Name"
        value={formData.middleName || ""}
        onChange={handleInputChange}
      />
      <input
        type="text"
        name="surname"
        placeholder="Surname"
        value={formData.surname || ""}
        onChange={handleInputChange}
      />
      <input
        type="text"
        name="homeAddress"
        placeholder="Home Address"
        value={formData.homeAddress || ""}
        onChange={handleInputChange}
      />

      <div className="mt-2">
        <label htmlFor="dateOfBirth" className="block font-semibold text-gray-700">Birth Date</label>
        <input
          type="date"
          name="dateOfBirth"
          id="dateOfBirth"
          value={formData.dateOfBirth || ""}
          onChange={handleInputChange}
        />
      </div>

      <div className="mt-2">
        <label htmlFor="dateOfRegistration" className="block font-semibold text-gray-700">Registration Date</label>
        <input
          type="date"
          name="dateOfRegistration"
          id="dateOfRegistration"
          value={formData.dateOfRegistration || ""}
          onChange={handleInputChange}
        />
      </div>

      <div className="mt-4">
        <label className="block font-semibold text-gray-700">Matric No: _23120112023</label>
        <div className="mt-2">
          <label className="inline-flex items-center mr-4">
            <input
              type="radio"
              name="matricNo"
              value="yes"
              checked={formData.matricNo === true}
              onChange={handleInputChange}
            />
            <span className="ml-2">Yes</span>
          </label>
          <label className="inline-flex items-center">
            <input
              type="radio"
              name="matricNo"
              value="no"
              checked={formData.matricNo === false}
              onChange={handleInputChange}
            />
            <span className="ml-2">No</span>
          </label>
        </div>
      </div>

      <button onClick={handleSubmit}>
        {editingCustomer ? "Update" : "Submit"}
      </button>
    </div>
  );
};

export default CustomerForm;
