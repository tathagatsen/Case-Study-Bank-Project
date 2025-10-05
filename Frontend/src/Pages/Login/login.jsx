import React, { useState } from "react";
import { set, useForm } from "react-hook-form";
import { Link } from "react-router"; // use react-router-dom for v6+
import { useNavigate } from "react-router";
import toast from "react-hot-toast";
import { loginUser } from "../../api/UserApi";
import OtpModal from "./otpModal"; // adjust path

// Set cookie (expires after 1 hour)


export default function Login() {
  const navigate = useNavigate();
   const [otpOpen, setOtpOpen] = useState(false);
  const [enteredEmail, setEnteredEmail] = useState("");

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = (data) => {
    loginUser({email: data.email, password: data.password})
      .then((response) => {
        if (response.data) {
          setEnteredEmail(data.email);
          setOtpOpen(true);
        } else {
          toast.error("Invalid email or password. Please try again.");
        }
      })
      .catch((error) => {
        toast.error("An error occurred during login. Please try again.");
      });
  }
  return (
    <>
      {/*
        This example requires updating your template:

        ```
        <html class="h-full bg-white">
        <body class="h-full">
        ```
      */}
      <div className="h-full flex justify-center items-center">
        <div className="flex-1  shadow-lg rounded-lg flex flex-col justify-center py-12 px-4 sm:px-6 lg:flex-none lg:px-20 xl:px-24">
          <div className="mx-auto w-full max-w-sm lg:w-96">
            <div>
              <img
                className=" w-full"
                src="/simple-bank-logo.png"
                alt="Simple Bank"
              />
            </div>

            <div className="mt-8">
              <div className="mt-6">
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                  <div>
                    <label
                      htmlFor="email"
                      className="block text-sm font-medium text-gray-700"
                    >
                      Email address
                    </label>
                    <div className="mt-1">
                      <input
                        id="email"
                        autoComplete="email"
                        {...register("email", {
                          required: "Email is required",
                          pattern: {
                            value: /\S+@\S+\.\S+/,
                            message: "Invalid email format",
                          },
                        })}
                        className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                      />
                      {errors.email && (
                        <span className="text-red-500 text-xs">
                          {errors.email.message}
                        </span>
                      )}
                    </div>
                  </div>

                  <div className="space-y-1">
                    <label
                      htmlFor="password"
                      className="block text-sm font-medium text-gray-700"
                    >
                      Password
                    </label>
                    <div className="mt-1">
                      <input
                        id="password"
                        type="password"
                        autoComplete="current-password"
                        {...register("password", {
                          required: "Password is required",
                          minLength: {
                            value: 6,
                            message: "Password must be at least 6 characters",
                          },
                        })}
                        className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                      />
                      {errors.password && (
                        <span className="text-red-500 text-xs">
                          {errors.password.message}
                        </span>
                      )}
                    </div>
                  </div>

                  <div className="flex items-center justify-between">
                    <div className="flex items-center">
                      <input
                        id="remember-me"
                        name="remember-me"
                        type="checkbox"
                        className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                      />
                      <label
                        htmlFor="remember-me"
                        className="ml-2 block text-sm text-gray-900"
                      >
                        Remember me
                      </label>
                    </div>

                    <div className="text-sm">
                      <a
                        href="#"
                        className="font-medium text-indigo-600 hover:text-indigo-500"
                      >
                        Forgot your password?
                      </a>
                    </div>
                  </div>

                  <div>
                    <button
                      type="submit"
                      className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    >
                      Login
                    </button>
                  </div>
                </form>
                <div className="text-sm text-center mt-2">
                  <Link
                    to="/register"
                    className="font-medium text-indigo-600 hover:text-indigo-500"
                  >
                    Didn't register? Register now
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
        
      </div>
      <OtpModal open={otpOpen} email={enteredEmail} onClose={() => setOtpOpen(false)} />
   
    </>
  );
}
