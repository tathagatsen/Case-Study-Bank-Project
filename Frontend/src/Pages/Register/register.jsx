import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router"; // Use react-router-dom for v6+
import toast from "react-hot-toast";
import { registerUser } from "../../api/UserApi";
import OtpModal from "./otpModal";


export default function Register() {
     const [otpOpen, setOtpOpen] = useState(false);
     const [enteredEmail, setEnteredEmail] = useState("");
  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  // Form submit handler
  const onSubmit = (data) => {
    console.log("Form Submitted:", data);
    
    // console.log(otpOpen)
    registerUser({...data, role: "USER"})
      .then((response) => {
        console.log("Registration successful:", response.data);
        if (response.data) {
          setEnteredEmail(data.email);
          setOtpOpen(true);
        } else {
          toast.error("Registration failed! Please try again.");
        }
        
        // Redirect to login or another page if needed
      })
      .catch((error) => {
        console.error("Registration error:", error);
        toast.error("Registration failed. Please try again.");
      });
    // API logic here
  };

  return (
    <>
      <div className="min-h-full flex items-center justify-center">
        <div className="flex-1 flex shadow-lg rounded-lg flex-col justify-center py-12 px-4 sm:px-6 lg:flex-none lg:px-20 xl:px-24">
          <div className="mx-auto w-full max-w-sm lg:w-96">
            <div>
              <img
                className="w-full"
                src="/simple-bank-logo.png"
                alt="Simple Bank"
              />
            </div>
            <div className="mt-8">
              <div className="mt-6">
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                  <div className="flex gap-4">
                    <div className="w-1/2">
                      <label htmlFor="firstName" className="block text-sm font-medium text-gray-700">
                        First Name
                      </label>
                      <input
                        id="firstName"
                        {...register("firstName", { required: "First name is required" })}
                        className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                      />
                      {errors.firstName && <span className="text-red-500 text-xs">{errors.firstName.message}</span>}
                    </div>
                    <div className="w-1/2">
                      <label htmlFor="lastName" className="block text-sm font-medium text-gray-700">
                        Last Name
                      </label>
                      <input
                        id="lastName"
                        {...register("lastName", { required: "Last name is required" })}
                        className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                      />
                      {errors.lastName && <span className="text-red-500 text-xs">{errors.lastName.message}</span>}
                    </div>
                  </div>
                  <div>
                    <label htmlFor="dob" className="block text-sm font-medium text-gray-700">
                      Date of Birth
                    </label>
                    <input
                      id="dob"
                      type="date"
                      {...register("dob", { required: "Date of birth is required" })}
                      className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    />
                    {errors.dob && <span className="text-red-500 text-xs">{errors.dob.message}</span>}
                  </div>
                  <div>
                    <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                      Email address
                    </label>
                    <input
                      id="email"
                      type="text"
                      autoComplete="email"
                      {...register("email", {
                        required: "Email is required",
                        pattern: {
                          value: /\S+@\S+\.\S+/,
                          message: "Entered value does not match email format"
                        }
                      })}
                      className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    />
                    {errors.email && <span className="text-red-500 text-xs">{errors.email.message}</span>}
                  </div>
                  <div>
                    <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                      Password
                    </label>
                    <input
                      id="password"
                      type="password"
                      autoComplete="current-password"
                      {...register("password", {
                        required: "Password is required",
                        minLength: { value: 6, message: "Password must have at least 6 characters" }
                      })}
                      className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    />
                    {errors.password && <span className="text-red-500 text-xs">{errors.password.message}</span>}
                  </div>
                  <div>
                    <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-700">
                      Phone Number
                    </label>
                    <input
                      id="phoneNumber"
                      type="tel"
                      {...register("phoneNumber", {
                        required: "Phone number is required",
                        pattern: {
                          value: /^[0-9+\- ]{7,15}$/,
                          message: "Enter a valid phone number"
                        }
                      })}
                      className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    />
                    {errors.phone && <span className="text-red-500 text-xs">{errors.phone.message}</span>}
                  </div>
                  <div>
                    <label htmlFor="address" className="block text-sm font-medium text-gray-700">
                      Address
                    </label>
                    <textarea
                      id="address"
                      rows="3"
                      {...register("address", { required: "Address is required" })}
                      className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    />
                    {errors.address && <span className="text-red-500 text-xs">{errors.address.message}</span>}
                  </div>
                  <div>
                    <button
                      type="submit"
                      className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    >
                      Register
                    </button>
                  </div>
                </form>
                <div className="text-sm text-center mt-2">
                  <Link
                    to="/login"
                    className="font-medium text-indigo-600 hover:text-indigo-500"
                  >
                    Already registered? Login here
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      {/* <OtpModal isOpen={true} onClose={() => setOtpOpen(false)} enteredEmail={enteredEmail} /> */}
      <OtpModal open={otpOpen} onClose={() => setOtpOpen(false)} email={enteredEmail} />
    </>
  );
}